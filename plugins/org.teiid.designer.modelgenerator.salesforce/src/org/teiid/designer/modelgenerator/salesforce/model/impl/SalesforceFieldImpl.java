/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.modelgenerator.salesforce.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.teiid.designer.modelgenerator.salesforce.model.SalesforceField;

import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;

/**
 * @since 8.0
 */
public class SalesforceFieldImpl implements SalesforceField {

    public Field salesforceField;

    public SalesforceFieldImpl( Field field ) {
        salesforceField = field;
    }

    @Override
    public String getLabel() {
        return salesforceField.getLabel();
    }

    @Override
    public int getLength() {
        return salesforceField.getLength();
    }

    @Override
    public String getName() {
        return salesforceField.getName();
    }

    @Override
    public String getType() {
        return salesforceField.getType().value();
    }

    @Override
    public boolean isPrimaryKey() {
        return salesforceField.getType().value() == "id"; //$NON-NLS-1$
    }

    @Override
    public int getDigits() {
        return salesforceField.getDigits();
    }

    @Override
    public int getPrecision() {
        return salesforceField.getPrecision();
    }

    @Override
    public int getScale() {
        return salesforceField.getScale();
    }

    @Override
    public boolean isUpdateable() {
        return salesforceField.isUpdateable();
    }

    @Override
    public boolean isAuditField() {
        return isAuditField(salesforceField.getName());
    }

    public static boolean isAuditField( String name ) {
        boolean result = false;
        if (name.equals(SalesforceField.AUDIT_FIELD_CREATED_BY_ID) || name.equals(SalesforceField.AUDIT_FIELD_CREATED_DATE)
            || name.equals(SalesforceField.AUDIT_FIELD_LAST_MODIFIED_BY_ID)
            || name.equals(SalesforceField.AUDIT_FIELD_LAST_MODIFIED_DATE)
            || name.equals(SalesforceField.AUDIT_FIELD_SYSTEM_MOD_STAMP)) {
            result = true;
        }
        return result;
    }

    @Override
    public List<String> getAllowedValues() {
        List<String> result = new ArrayList<String>();
        List<PicklistEntry> entries = salesforceField.getPicklistValues();
        if (null != entries) {
            for (int i = 0; i < entries.size(); i++) {
                PicklistEntry entry = entries.get(i);
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    public boolean isSearchable() {
        return salesforceField.isFilterable();
    }

    @Override
    public boolean isCalculated() {
        return salesforceField.isCalculated();
    }

    @Override
    public boolean isCustom() {
        return salesforceField.isCustom();
    }

    @Override
    public boolean isDefaultedOnCreate() {
        return salesforceField.isDefaultedOnCreate();
    }

    @Override
    public boolean isRestrictedPicklist() {
        return salesforceField.isRestrictedPicklist();
    }

}
