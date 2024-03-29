/*
 * EnumKeyValueConfigurationBinaryObjectParserTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfiguration;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationDataType;
import java.time.Instant;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link EnumKeyValueConfigurationBinaryObjectParser}.
 *  
 * @author patrick
 */
public class EnumKeyValueConfigurationBinaryObjectParserTest {
    private static final String BRACE_START = "{";
    private static final String BRACE_END = "}";
    private static final String PIPE = "|";
    private static final String SPACE = "   ";
    private static final String NAME = "name";
    private static final String MIME_TYPE = "mime-type";
    private static final String TIMESTAMP_STRING = "2021-03-15T08:59:22.123Z";
    private static final Instant TIMESTAMP = DateUtil.getInstance().parseTimestamp(TIMESTAMP_STRING);
    //private static final String MESSAGE = "  my file content  "; 
    private static final String MESSAGE_ENCODED = "ICBteSBmaWxlIGNvbnRlbnQgIA=="; 


    /**
     * Test empty or null
     */
    @Test
    public void testEmptyOrNull() {
        assertEqualsBinaryObject(null, null, null, null, null);
        assertEqualsBinaryObject(null, null, null, null, "");
        assertEqualsBinaryObject(null, null, null, null, "    ");
    }


    /**
     * Test empty or null
     */
    @Test
    public void test() {
        // {text/plain}VGV4dAo=
        assertEqualsBinaryObject(null, null, MIME_TYPE, MESSAGE_ENCODED, BRACE_START + MIME_TYPE  + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObject(null, null, MIME_TYPE, MESSAGE_ENCODED, SPACE + BRACE_START + SPACE + MIME_TYPE  + SPACE + BRACE_END + SPACE + MESSAGE_ENCODED);
        
        // {text/plain}  
        assertEqualsBinaryObject(null, null, MIME_TYPE, null, BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObject(null, null, MIME_TYPE, null, SPACE + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE);
        
        // VGV4dAo=
        assertEqualsBinaryObject(null, null, null, MESSAGE_ENCODED, MESSAGE_ENCODED);
        assertEqualsBinaryObject(null, null, null, MESSAGE_ENCODED, SPACE + MESSAGE_ENCODED + SPACE);
        
        // 2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=
        assertEqualsBinaryObject(null, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObject(null, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        
        // 2021-03-15T08:59:22.123Z|{text/plain}
        assertEqualsBinaryObject(null, TIMESTAMP, MIME_TYPE, null, TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObject(null, TIMESTAMP, MIME_TYPE, null, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + SPACE + MIME_TYPE + BRACE_END);
        
        // 2021-03-15T08:59:22.123Z|VGV4dAo=
        assertEqualsBinaryObject(null, TIMESTAMP, null, MESSAGE_ENCODED, TIMESTAMP_STRING + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObject(null, TIMESTAMP, null, MESSAGE_ENCODED, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + MESSAGE_ENCODED + SPACE);

        // myfile.txt|{text/plain}VGV4dAo=
        assertEqualsBinaryObject(NAME, null, MIME_TYPE, MESSAGE_ENCODED, NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObject(NAME, null, MIME_TYPE, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);

        // myfile.txt|{text/plain}  
        assertEqualsBinaryObject(NAME, null, MIME_TYPE, null, NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObject(NAME, null, MIME_TYPE, null, "  " + NAME + SPACE + PIPE + SPACE  + BRACE_START + SPACE + MIME_TYPE + BRACE_END);
        
        // myfile.txt|VGV4dAo= 
        assertEqualsBinaryObject(NAME, null, null, MESSAGE_ENCODED, NAME + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObject(NAME, null, null, MESSAGE_ENCODED, NAME + SPACE + PIPE + SPACE + MESSAGE_ENCODED + SPACE);

        // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo= 
        assertEqualsBinaryObject(NAME, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObject(NAME, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE  + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE + MESSAGE_ENCODED);

        // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain} 
        assertEqualsBinaryObject(NAME, TIMESTAMP, MIME_TYPE, null, NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObject(NAME, TIMESTAMP, MIME_TYPE, null, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE);
        
        // myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo= 
        assertEqualsBinaryObject(NAME, TIMESTAMP, null, MESSAGE_ENCODED, NAME + PIPE + TIMESTAMP_STRING + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObject(NAME, TIMESTAMP, null, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + MESSAGE_ENCODED);
    }

    
    /**
     * Test empty or null
     */
    @Test
    public void testMerge() {
        final String refName = "refName";
        final String refMimeType = "refMime-type";
        final Instant refTimestamp = DateUtil.getInstance().parseTimestamp("2036-03-15T08:59:22.123Z");

        // {text/plain}VGV4dAo=
        assertEqualsBinaryObjectMerge(refName, refTimestamp, MIME_TYPE, MESSAGE_ENCODED, BRACE_START + MIME_TYPE  + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(refName, refTimestamp, MIME_TYPE, MESSAGE_ENCODED, SPACE + BRACE_START + SPACE + MIME_TYPE  + SPACE + BRACE_END + SPACE + MESSAGE_ENCODED);
        
        // {text/plain}  
        assertEqualsBinaryObjectMerge(refName, refTimestamp, MIME_TYPE, null, BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObjectMerge(refName, refTimestamp, MIME_TYPE, null, SPACE + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE);
        
        // VGV4dAo=
        assertEqualsBinaryObjectMerge(refName, refTimestamp, refMimeType, MESSAGE_ENCODED, MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(refName, refTimestamp, refMimeType, MESSAGE_ENCODED, SPACE + MESSAGE_ENCODED + SPACE);
        
        // 2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        
        // 2021-03-15T08:59:22.123Z|{text/plain}
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, MIME_TYPE, null, TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, MIME_TYPE, null, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + SPACE + MIME_TYPE + BRACE_END);
        
        // 2021-03-15T08:59:22.123Z|VGV4dAo=
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, null, MESSAGE_ENCODED, TIMESTAMP_STRING + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(refName, TIMESTAMP, null, MESSAGE_ENCODED, SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + MESSAGE_ENCODED + SPACE);

        // myfile.txt|{text/plain}VGV4dAo=
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, MIME_TYPE, MESSAGE_ENCODED, NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, MIME_TYPE, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);

        // myfile.txt|{text/plain}  
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, MIME_TYPE, null, NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, MIME_TYPE, null, "  " + NAME + SPACE + PIPE + SPACE  + BRACE_START + SPACE + MIME_TYPE + BRACE_END);
        
        // myfile.txt|VGV4dAo= 
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, refMimeType, MESSAGE_ENCODED, NAME + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(NAME, refTimestamp, refMimeType, MESSAGE_ENCODED, NAME + SPACE + PIPE + SPACE + MESSAGE_ENCODED + SPACE);

        // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo= 
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, MIME_TYPE, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE  + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE + MESSAGE_ENCODED);

        // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain} 
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, MIME_TYPE, null, NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, MIME_TYPE, null, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + BRACE_START + SPACE + MIME_TYPE + SPACE + BRACE_END + SPACE);
        
        // myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo= 
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, refMimeType, MESSAGE_ENCODED, NAME + PIPE + TIMESTAMP_STRING + PIPE + MESSAGE_ENCODED);
        assertEqualsBinaryObjectMerge(NAME, TIMESTAMP, refMimeType, MESSAGE_ENCODED, SPACE + NAME + SPACE + PIPE + SPACE + TIMESTAMP_STRING + SPACE + PIPE + SPACE + MESSAGE_ENCODED);
    }

    
    /**
     * Test empty or null
     */
    @Test
    public void testFilenameHeader() {
        String filenameHeader = "<my:/resource/>";

        // <my:/resource/<name -> will be interpreted as data
        assertNull(EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME).getName());
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME).getData());

        // <my:/resource/>name|{mimetype} -> will be interpreted as name and mime type
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END).getName());
        assertEquals(MIME_TYPE, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END).getMimetype());

        // <my:/resource/<name -> will be interpreted as data
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE).getData());
        
        // <my:/resource/>name|{mimetype} -> will be interpreted as name and mime type
        assertEqualsBinaryObject(filenameHeader + NAME, TIMESTAMP, MIME_TYPE, null, filenameHeader + NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
        
        
        filenameHeader = "my:/resource/";

        // <my:/resource/<name -> will be interpreted as data
        assertNull(EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME).getName());
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME).getData());

        // <my:/resource/>name|{mimetype} -> will be interpreted as name and mime type
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END).getName());
        assertEquals(MIME_TYPE, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE + BRACE_START + MIME_TYPE + BRACE_END).getMimetype());

        // <my:/resource/<name -> will be interpreted as data
        assertEquals(filenameHeader + NAME, EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(filenameHeader + NAME + PIPE).getData());
        
        // <my:/resource/>name|{mimetype} -> will be interpreted as name and mime type
        assertEqualsBinaryObject(filenameHeader + NAME, TIMESTAMP, MIME_TYPE, null, filenameHeader + NAME + PIPE + TIMESTAMP_STRING + PIPE + BRACE_START + MIME_TYPE + BRACE_END);
    }

    
    /**
     * Assert
     *
     * @param name thze name
     * @param timestamp the timestamp
     * @param mimetype the mime type
     * @param data the data
     * @param toParse the data to parse
     */
    private void assertEqualsBinaryObject(String name, Instant timestamp, String mimetype, String data, String toParse) {
        assertEquals(new EnumKeyValueConfigurationBinaryObject(name, timestamp, mimetype, data), 
                     EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(toParse));
    }


    /**
     * Assert
     *
     * @param name thze name
     * @param timestamp the timestamp
     * @param mimetype the mime type
     * @param data the data
     * @param toParse the data to parse
     */
    private void assertEqualsBinaryObjectMerge(String name, Instant timestamp, String mimetype, String data, String toParse) {

        EnumKeyValueConfigurationBinaryObject o = new EnumKeyValueConfigurationBinaryObject();
        o.setName(name);
        o.setTimestamp(timestamp);
        o.setMimetype(mimetype);
        //o.setData(data);

        EnumKeyValueConfiguration enumKeyValueConfiguration = new EnumKeyValueConfiguration();
        enumKeyValueConfiguration.setDataType(EnumKeyValueConfigurationDataType.BINARY);
        enumKeyValueConfiguration.setDefaultValue(EnumKeyValueConfigurationBinaryObjectParser.getInstance().format(o));
        
        String valueToParse = EnumKeyValueConfigurationBinaryObjectParser.getInstance().mergeDefaultValues(enumKeyValueConfiguration, toParse);
        
        assertEquals(new EnumKeyValueConfigurationBinaryObject(name, timestamp, mimetype, data), 
                     EnumKeyValueConfigurationBinaryObjectParser.getInstance().parse(valueToParse));
    }
}
