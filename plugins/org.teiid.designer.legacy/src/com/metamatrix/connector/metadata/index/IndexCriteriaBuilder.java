/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */

package com.metamatrix.connector.metadata.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.metamatrix.core.index.SimpleIndexUtil;
import org.teiid.core.util.ArgCheck;
import org.teiid.core.util.StringUtil;
import com.metamatrix.modeler.core.index.IndexConstants;
import com.metamatrix.modeler.core.metadata.runtime.DatatypeRecord;
import com.metamatrix.modeler.core.metadata.runtime.MetadataRecord;
import com.metamatrix.modeler.core.metadata.runtime.PropertyRecord;


/** 
 * This is used by MetadataConnector to build criteria patterns used to query index files.
 * This has generic methods to build criteria that matches a particular header patterns that
 * is part of most index records. It also can be extended to create a pattern that matches
 * the record pattern for a given record type. 
 * @since 4.3
 */
public class IndexCriteriaBuilder {
    
    private static final String RECORD_STRING_SPACE = StringUtil.Constants.EMPTY_STRING+IndexConstants.RECORD_STRING.SPACE;
    private static final String RECORD_STRING_TRUE  = StringUtil.Constants.EMPTY_STRING+IndexConstants.RECORD_STRING.TRUE;
    private static final String RECORD_STRING_FALSE = StringUtil.Constants.EMPTY_STRING+IndexConstants.RECORD_STRING.FALSE;
   
    /**
     * Return the prefix match string constructed from a map of
     * match criteria values.  The match criteria map is keyed
     * on field names found in the MetadataRecord. 
     * @param indexName The name of the index file that would be queried using the prefix returned.
     * @param criteria the map of match criteria to use
     * @return the prefix string
     */    
    public static String getMatchPrefix(final String indexName, final Map criteria) {
        ArgCheck.isNotEmpty(indexName);
        ArgCheck.isNotNull(criteria);
        
        // get updated criteria map
        Map updatedCriteria = getUpdatedCriteria(indexName, criteria);
        
        // if there is no criteria return prefix with just the record type
        // if available from indexName
        if(criteria.isEmpty()) {
            return getRecordDefaultMatchPrefix(updatedCriteria);
        }

        // datatype records have a different pattern that most index records, create
        // a match prefix specific to datatypes
        if(indexName.equalsIgnoreCase(IndexConstants.INDEX_NAME.DATATYPES_INDEX)) {
            return getDatatypeRecordMatchPrefix(updatedCriteria);
        // property records have a different pattern that most index records, create
        // a match prefix specific to properties
        } else if(indexName.equalsIgnoreCase(IndexConstants.INDEX_NAME.PROPERTIES_INDEX)) {
            return getPropertiesRecordMatchPrefix(updatedCriteria);            
        // model records have a different pattern that most index records, create
        // a match prefix specific to models
        } else if(indexName.equalsIgnoreCase(IndexConstants.INDEX_NAME.MODELS_INDEX)) {
            return getModelRecordMatchPrefix(updatedCriteria);            
        }
        // for all other records get the generic header prefix
        // append header prefix first
        return getRecordHeaderMatchPrefix(updatedCriteria);
    }

    /**
     * Return the pattern match string constructed from a map of
     * match criteria values.  The match criteria map is keyed
     * on field names found in the MetadataRecord. 
     * @param indexName The name of the index file that would be queried using the pattern returned.
     * @param criteria the map of match criteria to use
     * @return the pattern string
     */
    public static String getMatchPattern(final String indexName, final Map criteria) {
        ArgCheck.isNotEmpty(indexName);
        ArgCheck.isNotNull(criteria);

        // get updated criteria map
        Map updatedCriteria = getUpdatedCriteria(indexName, criteria);


        final StringBuffer sb = new StringBuffer(100);
        // if there is no criteria match everything
        if(criteria.isEmpty()) {
            sb.append(getRecordDefaultMatchPattern(updatedCriteria));
        } else {
            // datatype records have a different pattern that most index records, create
            // a match pattern specific to datatypes
            if(indexName.equalsIgnoreCase(IndexConstants.INDEX_NAME.DATATYPES_INDEX)) {
                sb.append(getDatatypeRecordMatchPattern(updatedCriteria));
            // property records have a different pattern that most index records, create
            // a match pattern specific to properties
            } else if(indexName.equalsIgnoreCase(IndexConstants.INDEX_NAME.PROPERTIES_INDEX)) {
                sb.append(getPropertiesRecordMatchPattern(updatedCriteria));            
            } else {
                // for all other records get the generic header pattern
                // append header pattern first
                sb.append(getRecordHeaderMatchPattern(updatedCriteria));
            }
        }
        // append a wildcard char to the end of the header contructed
        sb.append(IndexConstants.RECORD_STRING.MATCH_CHAR);
        // currently cannot match footern without knowing the number of tokens in each record
        //sb.append(getRecordFooterMatchPattern(tmp));

        return sb.toString();
    }
    
    /**
     * Return a map of crteria with upper cased key names and include addition criteria
     * for record type if available.
     * @param indexName The name of the index file / may be used to look up recordtype criteria
     * @param criteria The criteria built from metadata query
     * @return The updated criteria map
     * @since 4.3
     */
    private static Map getUpdatedCriteria(final String indexName, final Map criteria) {

        final Map tmp = new HashMap(criteria.size());        
        // based on the indexName try to derive the recordtype and make it part of criteria
        if(getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD) == null) {
            String record_type = SimpleIndexUtil.getRecordTypeForIndexFileName(indexName);
            if(record_type != null) {
                MetadataLiteralCriteria literalCriteria = new MetadataLiteralCriteria(MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD, record_type);
                tmp.put(MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD.toUpperCase(), literalCriteria);
            }
        }        
        // Copy the map so that the keys, corresponding to field names in the
        // metadatarecord classes, can be converted to upper case
        for (Iterator iter = criteria.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry)iter.next();
            final String key = (String)entry.getKey();
            final Object value = entry.getValue();
            tmp.put(key.toUpperCase(),value);
        }

        return tmp;
    }
    
    /**
     * Return a collection of criteria maps with MetadataLiteralCriteria's, any
     * compound criteria are broken down into a collection of MetadataLiteralCriteria. 
     * @param criteria The criteria built from metadata query
     * @return Collection of maps with literal criteria
     * @since 4.3
     */
    public static Collection getLiteralCriteria(Map criteria) {
        // traverse criteria map and colect different criteria
        Collection inCriteriaCollect = new ArrayList();
        Map criteriaCopy = new HashMap(criteria);
        for (Iterator iter = criteria.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry)iter.next();
            final String key = (String)entry.getKey();
            final Object value = entry.getValue();
            if(value instanceof MetadataInCriteria) {
                inCriteriaCollect.add(value);
                criteriaCopy.remove(key);
            }
        }
        
        Collection criteriaCollect = new ArrayList();
        criteriaCollect.add(criteriaCopy);
        // breat down InCriteria to Literal criteria
        for(Iterator iter2 = inCriteriaCollect.iterator(); iter2.hasNext();) {
            criteriaCollect = getLiteralCriteria((MetadataInCriteria) iter2.next(), criteriaCollect);
        }
        
        return criteriaCollect;
    }

    /**
     * MetadataInCriteria objects are broken down into collection MetadataLiteralCriteria objects 
     * @param inCriteria MetadataInCriteria object
     * @param criteriaCollect Collection of maps of literal criteria
     * @return Collection of maps of literal criteria
     * @since 4.3
     */
    private static Collection getLiteralCriteria(MetadataInCriteria inCriteria, Collection criteriaCollect) {
        Collection critCollection = new ArrayList();
        Collection litCriteria = inCriteria.getLiteralCriteria();
        for(Iterator iter1 = criteriaCollect.iterator(); iter1.hasNext();) {  
            Map criteria = (Map) iter1.next();
            for(final Iterator critIter = litCriteria.iterator(); critIter.hasNext();) {
                Map copyCriteria = new HashMap(criteria);
                copyCriteria.put(inCriteria.getFieldName().toUpperCase(), critIter.next());
                critCollection.add(copyCriteria);
            }
        }
        return critCollection;
    }

    /**
     * Construct a prefix string based on the values found in the map
     * Every index records contain a header portion of the form:  
     * Header : recordType|
     * @param criteria the map of match criteria to use
     * @return The prefix string matching the header
     * @since 4.3
     */
    private static String getRecordDefaultMatchPrefix(final Map criteria) {

        String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
        if(isPrefixCriteriaString(recordTypeCriteria)) {
            final StringBuffer sb = new StringBuffer(2);
            // record type
            sb.append(recordTypeCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            
            return sb.toString();
        }
        return null;
    }
    
    /**
     * Construct a prefix string based on the values found in the map
     * Every index records contain a header portion of the form:  
     * Header : recordType|
     * @param criteria the map of match criteria to use
     * @return The pattern string matching the header
     * @since 4.3
     */
    private static String getRecordDefaultMatchPattern(final Map criteria) {
        final StringBuffer sb = new StringBuffer(2);
        // record type
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD, sb);
        return sb.toString();
    }    

    /**
     * Construct a pattern string based on the values found in the map
     * Most index records contain a header portion of the form:  
     * Header : recordType|upperFullName|objectID|fullName|nameInSource|parentObjectID
     * @param criteria the map of match criteria to use
     * @return The pattern strig matching the header
     * @since 4.3
     */
    private static String getRecordHeaderMatchPattern(final Map criteria) {

        final StringBuffer sb = new StringBuffer(100);
        // record type
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD, sb);
        // name matching is already done in fourth field, just a wild card
        appendMultiMatchField(sb);
        // uuid
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.UUID_FIELD, sb);
        // fullName or ModelName and/or Name
        // if fullName is not available in the criteria, use the ModerName and/or Name in criteria
        String fullNameCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.FULL_NAME_FIELD);
        if(fullNameCriteria == null) {
            String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
            // if its a model or vdb record only criteria possible is on Name
            if(recordTypeCriteria != null &&
                (recordTypeCriteria.equalsIgnoreCase(StringUtil.Constants.EMPTY_STRING+IndexConstants.RECORD_TYPE.MODEL))) {
                appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.NAME_FIELD, sb);
            } else {
                String modelNameCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.MODEL_NAME_FIELD);
                if(modelNameCriteria != null) {
                    sb.append(modelNameCriteria);
                    sb.append(IndexConstants.NAME_DELIM_CHAR);
                }
                sb.append(IndexConstants.RECORD_STRING.MATCH_CHAR);            
                String nameCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.NAME_FIELD);
                if(nameCriteria != null) {
                    sb.append(IndexConstants.NAME_DELIM_CHAR);
                    sb.append(nameCriteria);
                }
                sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);                
            }
        } else {
            appendFieldValue(fullNameCriteria, sb);            
        }        
        // name in source
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.NAME_IN_SOURCE_FIELD,sb);
        // parent uuid
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.PARENT_UUID_FIELD, sb);
        
        return sb.toString();
    }
    
    /**
     * Construct a prefix string based on the values found in the map
     * Most index records contain a header portion of the form:  
     * Header : recordType|upperFullName|objectID|fullName|nameInSource|parentObjectID
     * @param criteria the map of match criteria to use
     * @return The prefix string matching the header
     * @since 4.3
     */
    private static String getRecordHeaderMatchPrefix(final Map criteria) {

        String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
        String fullNameCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.FULL_NAME_FIELD);        
        if(isPrefixCriteriaString(recordTypeCriteria) && isPrefixCriteriaString(fullNameCriteria)) {
            final StringBuffer sb = new StringBuffer(30);
            sb.append(recordTypeCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            // full name
            sb.append(fullNameCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            
            return sb.toString();
        }
        return null;
    }
//    /**
//     * Construct a pattern string based on the values found in the map
//     * Most index records contain a footer portion of the form:
//     * Header : modelPath|name
//     * @param criteria the map of match criteria to use
//     * @return The pattern strig matching the footer
//     * @since 4.3
//     */
//    private static String getRecordFooterMatchPattern(final Map criteria) {
//
//        final StringBuffer sb = new StringBuffer(100);
//        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.PATH_IN_MODEL_FIELD, sb);
//        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.NAME_FIELD,sb);        
//
//        return sb.toString();       
//    }
//
//    /**
//     * Try finding the given fieldName among the keys of the given criteria map, if available
//     * include its value in the pattern string returned else include a wild card match char.  
//     * @param criteria the map of match criteria to use
//     * @param fieldName The name of the field to look for in the criteria
//     * @return The pattern string matching the fildName
//     * @since 4.3
//     */
//    private static String getMatchPatternForField(final Map criteria, final String fieldName) {
//        final StringBuffer sb = new StringBuffer(10);        
//        String fieldValue = getValueInCriteria(criteria, fieldName);
//        if (fieldValue != null) {
//            sb.append( fieldValue );
//        } else {
//            sb.append( IndexConstants.RECORD_STRING.MATCH_CHAR );
//        }
//        sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
//        return sb.toString();
//    }
    
    /**
     * Look for the value for the given field in the criteria map, translate it if
     * necessary to the format stored in index files and return it. 
     * @param criteria the map of match criteria to use
     * @param fieldName The name of the field to look for in the criteria
     * @return The value for the given field in the criteria
     * @since 4.3
     */
    protected static String getValueInCriteria(final Map criteria, String fieldName) {
        // use uppercase value as key
        fieldName = fieldName.toUpperCase();
        if (criteria.containsKey(fieldName) && criteria.get(fieldName) != null) {
            Object value = criteria.get(fieldName);
            // only literal criteria are handles here, all other criteria may
            // be used during post processing.
            if (value instanceof MetadataLiteralCriteria) {
                MetadataLiteralCriteria literalCriteria = (MetadataLiteralCriteria) value;
                Object literalValue = literalCriteria.getFieldValue();
                if (literalValue == null) {
                    return RECORD_STRING_SPACE;
                } else if (literalValue instanceof Boolean) {
                    Boolean booleanValue = (Boolean) literalValue;
                    if (booleanValue.booleanValue()) {
                        return RECORD_STRING_TRUE;
                    }
                    return RECORD_STRING_FALSE;
                }
                return literalValue.toString();
            }
        }
        return null;
    }
    
    /**
     * Try finding the given fieldName among the keys of the given criteria map, if available
     * include its value in the pattern string returned else include a wild card match char.  
     * @param criteria the map of match criteria to use
     * @param fieldName The name of the field to look for in the criteria
     * @param sb The string buffer to append match patern to
     * @since 4.3
     */    
    private static void appendCriteriaValue(final Map criteria, final String fieldName, final StringBuffer sb) {
        String fieldValue = getValueInCriteria(criteria, fieldName);
        appendFieldValue(fieldValue, sb);
    }
    
    /**
     * Applend the value of a field and a delimiter if the value is not null include a wild card match char.  
     * @param criteria the map of match criteria to use
     * @param fieldValue The value of the field
     * @param sb The string buffer to append match patern to
     * @since 4.3
     */    
    private static void appendFieldValue(final String fieldValue, final StringBuffer sb) {
        if (fieldValue != null) {
            sb.append( fieldValue );
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);            
        } else {
            appendMultiMatchField(sb);
        }        
    }
    
    /**
     * Append a wild card char to macth any string to the given string buffer. 
     * @param sb The string buffer to append match patern to
     * @since 4.3
     */
    private static void appendMultiMatchField(final StringBuffer sb) {
        appendFieldValue(IndexConstants.RECORD_STRING.MATCH_CHAR_STRING , sb);
    }

    /**
     * This create match prefix specific to model records, gets appended at the end of the
     * header. The model records are of the form:
     * header|maxSetSize|modelType|primaryMetamodelUri|isVisible *|footer|
     * @param criteria the map of match criteria to use 
     * @return The match prefix for model records
     * @since 4.3
     */
    private static String getModelRecordMatchPrefix(final Map criteria) {
        String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
        String nameCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.NAME_FIELD);        
        if(isPrefixCriteriaString(recordTypeCriteria) && isPrefixCriteriaString(nameCriteria)) {
            final StringBuffer sb = new StringBuffer(30);
            // uuidCriteria
            sb.append(recordTypeCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            // name
            sb.append(nameCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);            

            return sb.toString();
        }
        return null;
    }

    /**
     * This create match pattern specific to property records, gets appended at the end of the
     * header. The properties records are of the form:
     * recordType|objectID|propertyName|value|isExtention|footer|
     * @param criteria the map of match criteria to use 
     * @return The match pattern for properties records
     * @since 4.3
     */
    private static String getPropertiesRecordMatchPattern(final Map criteria) {
        final StringBuffer sb = new StringBuffer(100);
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD, sb);
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.UUID_FIELD, sb);
        appendCriteriaValue(criteria, PropertyRecord.MetadataFieldNames.PROPERTY_NAME_FIELD, sb);
        appendCriteriaValue(criteria, PropertyRecord.MetadataFieldNames.PROPERTY_VALUE_FIELD, sb);
        
        return sb.toString();        
    }
    
    /**
     * This create match prefix specific to property records, gets appended at the end of the
     * header. The properties records are of the form:
     * recordType|objectID|propertyName|value|isExtention|footer|
     * @param criteria the map of match criteria to use 
     * @return The match prefix for properties records
     * @since 4.3
     */
    private static String getPropertiesRecordMatchPrefix(final Map criteria) {
        String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
        String uuidCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.UUID_FIELD);        
        if(isPrefixCriteriaString(recordTypeCriteria) && isPrefixCriteriaString(uuidCriteria)) {            
            final StringBuffer sb = new StringBuffer(100);
            // record type
            sb.append(recordTypeCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            // uuidCriteria
            sb.append(uuidCriteria.toLowerCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            return sb.toString();
        }
        return null;        
    }    
    
    /**
     * This create match pattern specific to datatype records, gets appended at the end of the
     * header. The datatype records are of the form:
     * recordType|datatypeID|basetypeID|fullName|objectID|nameInSource|varietyType|varietyProps|
     *            runtimeTypeName|javaClassName|type|searchType|nullType|booleanValues|length|precisionLength|
     *            scale|radix|primitiveTypeID|footer|
     * @param criteria the map of match criteria to use 
     * @return The match pattern for datatype records
     * @since 4.3
     */
    private static String getDatatypeRecordMatchPattern(final Map criteria) {
        final StringBuffer sb = new StringBuffer(100);
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD, sb);
        appendCriteriaValue(criteria, DatatypeRecord.MetadataFieldNames.DATA_TYPE_UUID, sb);
        appendCriteriaValue(criteria, DatatypeRecord.MetadataFieldNames.BASE_TYPE_UUID, sb);
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.NAME_FIELD,sb);        
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.UUID_FIELD, sb);
        appendCriteriaValue(criteria, MetadataRecord.MetadataFieldNames.NAME_IN_SOURCE_FIELD,sb);
        appendMultiMatchField(sb);
        appendMultiMatchField(sb);        
        appendCriteriaValue(criteria, DatatypeRecord.MetadataFieldNames.RUN_TYPE_NAME, sb);
        
        return sb.toString();    
    }
    
    /**
     * This create match prefix specific to datatype records, gets appended at the end of the
     * header. The datatype records are of the form:
     * recordType|datatypeID|basetypeID|fullName|objectID|nameInSource|varietyType|varietyProps|
     *            runtimeTypeName|javaClassName|type|searchType|nullType|booleanValues|length|precisionLength|
     *            scale|radix|primitiveTypeID|footer|
     * @param criteria the map of match criteria to use 
     * @return The match prefix for datatype records
     * @since 4.3
     */
    private static String getDatatypeRecordMatchPrefix(final Map criteria) {
        String recordTypeCriteria = getValueInCriteria(criteria, MetadataRecord.MetadataFieldNames.RECORD_TYPE_FIELD);
        String uuidCriteria = getValueInCriteria(criteria, DatatypeRecord.MetadataFieldNames.DATA_TYPE_UUID);
        if(isPrefixCriteriaString(recordTypeCriteria) && isPrefixCriteriaString(uuidCriteria)) {
            final StringBuffer sb = new StringBuffer(100);
            // record type
            sb.append(recordTypeCriteria.toUpperCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            // uuidCriteria
            sb.append(uuidCriteria.toLowerCase());
            sb.append(IndexConstants.RECORD_STRING.RECORD_DELIMITER);
            
            return sb.toString();
        }
        return null;
    }

    /**
     * Check if the given criteria string can be used in a match prefix.
     * Makes sure its not empty and there are no wild card charachters in it.  
     * @param criteria Teh string to be used in a preifix
     * @return true if can be used else false
     * @since 4.3
     */
    private static boolean isPrefixCriteriaString(final String criteria) {
        boolean prefixCriteria = true;
        if(StringUtil.isEmpty(criteria)) {
            prefixCriteria = false;
        } else {
            if(criteria.indexOf(IndexConstants.RECORD_STRING.MATCH_CHAR) != -1) {
                prefixCriteria = false;
            } else if(criteria.indexOf(IndexConstants.RECORD_STRING.SINGLE_CHAR_MATCH) != -1) {
                prefixCriteria = false;
            }
        }
        return prefixCriteria;
    }
}