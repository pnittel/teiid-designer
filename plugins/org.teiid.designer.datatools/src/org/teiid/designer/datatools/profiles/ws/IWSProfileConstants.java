/*
 * JBoss, Home of Professional Open Source.
*
* See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
*
* See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
*/
package org.teiid.designer.datatools.profiles.ws;

import org.teiid.designer.ui.common.ICredentialsCommon;

/**
 * @since 8.0
 */
public interface IWSProfileConstants extends ICredentialsCommon {

	String TEIID_WS_CONNECTION_PROFILE_ID = "org.teiid.designer.datatools.profiles.ws.WSConnectionProfile";  //$NON-NLS-1$

    String TEIID_CATEGORY = "org.teiid.designer.import.category"; //$NON-NLS-1$
    

    /*
     * Teiid Data Source property key.
     * 
     * Currently only EndPoint is the only property provided by Data Tools connection profile that matches up.
     */
     String DS_ENDPOINT = "wsdlURI"; //$NON-NLS-1$
     String DS_SECURITY_TYPE = "SecurityType"; //$NON-NLS-1$";
     String DS_AUTH_USER_NAME = "AuthUserName"; //$NON-NLS-1$
     String DS_AUTH_PASSWORD = "AuthPassword"; //$NON-NLS-1$
     String DS_WS_SECURITY_CONFIG_URL = "WsSecurityConfigURL"; //$NON-NLS-1$
     String DS_WS_SECURITY_CONFIG_NAME = "WsSecurityConfigName"; //$NON-NLS-1$

     String SOURCE_ENDPOINT = "EndPoint"; //$NON-NLS-1$
     String SOAP_SERVICE_MODE = "DefaultServiceMode";  //$NON-NLS-1$
     String SOAP_BINDING = "DefaultBinding";  //$NON-NLS-1$

    /*
     * The Web Services Data Source object contains the following properties
     * 
     * connectionClass=org.my.custom.driver.Class
     * soapEndPoint=http://my.soap.endpoint.url
     * driverClassPath=org.my.first.jar;org.my.second.jar;
     * 
     * The only property that matches up with the teiid-connector-ws.jar definition is the soapEndPoint
     * 
     */
     String URL_PROP_ID = "EndPoint"; //$NON-NLS-1$
     String SOAP_ENDPOINT_KEY = "soapEndPoint"; //$NON-NLS-1$
     String CONNECTION_CLASS_KEY = "connectionClass"; //$NON-NLS-1$
     String DRIVER_CLASS_PATH_KEY = "driverClassPath"; //$NON-NLS-1$
     
     /**
      * Used by the WSDL Importer in 7.7.1 for the wsdl URI connection profile property key.
      * Should only be used for backward compatibilty
      */
     String LEGACY_WSDL_PROP_ID = "wsdlURI"; //$NON-NLS-1$
}
