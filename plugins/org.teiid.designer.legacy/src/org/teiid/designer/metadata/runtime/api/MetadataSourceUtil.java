package org.teiid.designer.metadata.runtime.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.teiid.core.TeiidException;
import org.teiid.core.util.ArgCheck;
import org.teiid.core.util.ObjectConverterUtil;
import org.teiid.core.util.StringUtil;
import org.teiid.designer.core.util.AssertionUtil;


/**
 * @since 8.0
 */
public class MetadataSourceUtil {

    public static String getFileContentAsString( String path,
                                                 MetadataSource iss ) throws TeiidException {
        File f = iss.getFile(path);
        if (f == null) {
            return null;
        }
        try {
            return ObjectConverterUtil.convertFileToString(f);
        } catch (IOException e) {
            throw new TeiidException(e, "MetadataSourceUtil.ioExceptionConvertingFileToString");
        }
    }

    /**
     * @throws TeiidException
     * @see org.teiid.designer.core.index.IndexSelector#getFileContent(java.lang.String, java.lang.String[],
     *      java.lang.String[])
     * @since 4.2
     */
    public static InputStream getFileContent( final String path,
                                              MetadataSource iss,
                                              final String[] tokens,
                                              final String[] tokenReplacements ) throws TeiidException {
        ArgCheck.isNotNull(tokens);
        ArgCheck.isNotNull(tokenReplacements);
        AssertionUtil.isEqual(tokens.length, tokenReplacements.length);
        String fileContents = getFileContentAsString(path, iss);
        if (fileContents != null) {
            for (int i = 0; i < tokens.length; i++) {
                final String token = tokens[i];
                final String tokenReplacement = tokenReplacements[i];
                fileContents = StringUtil.replaceAll(fileContents, token, tokenReplacement);
            }
            return new ByteArrayInputStream(fileContents.getBytes());
        }
        return null;
    }

}
