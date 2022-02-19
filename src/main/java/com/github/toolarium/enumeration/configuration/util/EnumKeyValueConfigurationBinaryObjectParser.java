/*
 * EnumKeyValueConfigurationBinaryObjectParser.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.enumeration.configuration.util;

import com.github.toolarium.enumeration.configuration.dto.EnumKeyValueConfigurationBinaryObject;
import com.github.toolarium.enumeration.configuration.dto.IEnumKeyValueConfigurationBinaryObject;
import java.time.Instant;
import java.time.format.DateTimeParseException;


/**
 * Parse for {@link IEnumKeyValueConfigurationBinaryObject}
 *  
 * @author patrick
 */
public final class EnumKeyValueConfigurationBinaryObjectParser {
    /** SEPARATOR */
    public static final String SEPARATOR = "|";
    
    /** BRACE START */
    public static final String BRACE_START = "{";
    
    /** BRACE END */
    public static final String BRACE_END = "}";
    private static final String ESCAPED_SEPARATOR = "\\" + SEPARATOR;


    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static class HOLDER {
        static final EnumKeyValueConfigurationBinaryObjectParser INSTANCE = new EnumKeyValueConfigurationBinaryObjectParser();
    }

    
    /**
     * Constructor
     */
    private EnumKeyValueConfigurationBinaryObjectParser() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static EnumKeyValueConfigurationBinaryObjectParser getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Parse a binary content.
     * Format: <code>[name]|[timestamp]|{mime-type}[content base64 encoded].</code>
     * The name, timestamp (according to RFC 3339), mime-type and the content  (base64 encoded content) are optional values. If the name, timestamp or content are present, 
     * they must be separated by a pipe character (|). The mime-type can be declared as header of the content. If there is no pipe character it's assumed its the content part.  
     * e.g. <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code> 
     *   or <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}</code> 
     *   or <code>myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo=</code> 
     *   or <code>2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code>
     *   or <code>2021-03-15T08:59:22.123Z|{text/plain}</code>
     *   or <code>2021-03-15T08:59:22.123Z|VGV4dAo=</code>
     *   or <code>myfile.txt|{text/plain}VGV4dAo=</code>
     *   or <code>myfile.txt|{text/plain}</code>  
     *   or <code>myfile.txt|VGV4dAo=</code> 
     *   or <code>{text/plain}VGV4dAo=</code>
     *   or <code>{text/plain}</code>  
     *   or <code>VGV4dAo=</code>  
     * 
     * @param input the input
     * @return the parsed object
     * @throws DateTimeParseException In case of an invalid date format
     */
    public IEnumKeyValueConfigurationBinaryObject parse(String input) throws DateTimeParseException {
        EnumKeyValueConfigurationBinaryObject enumKeyValueConfigurationBinaryObject = new EnumKeyValueConfigurationBinaryObject();
        if (input == null || input.isEmpty()) {
            return enumKeyValueConfigurationBinaryObject;
        }
        
        String[] binaryData = input.trim().split(ESCAPED_SEPARATOR);

        if (binaryData.length <= 1) {
            //  {text/plain}VGV4dAo=
            //  {text/plain}  
            //  VGV4dAo=  
            parseContent(enumKeyValueConfigurationBinaryObject, binaryData[0]);
        
        } else if (binaryData.length == 2) {
            // 2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=
            // 2021-03-15T08:59:22.123Z|{text/plain}
            // 2021-03-15T08:59:22.123Z|VGV4dAo=
            // myfile.txt|{text/plain}VGV4dAo=
            // myfile.txt|{text/plain}  
            // myfile.txt|VGV4dAo= 

            String s = binaryData[0].trim();
            Instant timestamp = parseTimestamp(s.trim());
            if (timestamp != null) {
                enumKeyValueConfigurationBinaryObject.setTimestamp(timestamp);
            } else {
                enumKeyValueConfigurationBinaryObject.setName(s.trim());
            }

            parseContent(enumKeyValueConfigurationBinaryObject, binaryData[1]);

        } else {
            // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo= 
            // myfile.txt|2021-03-15T08:59:22.123Z|{text/plain} 
            // myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo= 

            enumKeyValueConfigurationBinaryObject.setName(binaryData[0].trim());
            enumKeyValueConfigurationBinaryObject.setTimestamp(parseTimestamp(binaryData[1].trim()));
            parseContent(enumKeyValueConfigurationBinaryObject, binaryData[binaryData.length - 1]);
        }        
        
        return enumKeyValueConfigurationBinaryObject;
    }

    
    /**
     * Format an {@link IEnumKeyValueConfigurationBinaryObject} into string representation.
     * 
     * @param enumKeyValueConfigurationBinaryObject the object to format
     * @return the string representation:
     *         Format: <code>[name]|[timestamp]|{mime-type}[content base64 encoded].</code>
     *         The name, timestamp (according to RFC 3339), mime-type and the content  (base64 encoded content) are optional values. If the name, timestamp or content are present, 
     *         they must be separated by a pipe character (|). The mime-type can be declared as header of the content. If there is no pipe character it's assumed its the content part.  
     *         e.g. <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code> 
     *           or <code>myfile.txt|2021-03-15T08:59:22.123Z|{text/plain}</code> 
     *           or <code>myfile.txt|2021-03-15T08:59:22.123Z|VGV4dAo=</code> 
     *           or <code>2021-03-15T08:59:22.123Z|{text/plain}VGV4dAo=</code>
     *           or <code>2021-03-15T08:59:22.123Z|{text/plain}</code>
     *           or <code>2021-03-15T08:59:22.123Z|VGV4dAo=</code>
     *           or <code>myfile.txt|{text/plain}VGV4dAo=</code>
     *           or <code>myfile.txt|{text/plain}</code>  
     *           or <code>myfile.txt|VGV4dAo=</code> 
     *           or <code>{text/plain}VGV4dAo=</code>
     *           or <code>{text/plain}</code>  
     *           or <code>VGV4dAo=</code>  
     */
    public String format(IEnumKeyValueConfigurationBinaryObject enumKeyValueConfigurationBinaryObject) {
        StringBuilder builder = new StringBuilder();
        if (enumKeyValueConfigurationBinaryObject.getName() != null && !enumKeyValueConfigurationBinaryObject.getName().isBlank()) {
            builder.append(enumKeyValueConfigurationBinaryObject.getName());
            builder.append(EnumKeyValueConfigurationBinaryObjectParser.SEPARATOR);
        }
        
        if (enumKeyValueConfigurationBinaryObject.getTimestamp() != null) {
            builder.append(enumKeyValueConfigurationBinaryObject.getTimestamp());
        } else {
            builder.append(Instant.now());
        }

        
        builder.append(EnumKeyValueConfigurationBinaryObjectParser.SEPARATOR);

        if (enumKeyValueConfigurationBinaryObject.getMimetype() != null && !enumKeyValueConfigurationBinaryObject.getMimetype().isBlank()) {
            builder.append(EnumKeyValueConfigurationBinaryObjectParser.BRACE_START);
            builder.append(enumKeyValueConfigurationBinaryObject.getMimetype());
            builder.append(EnumKeyValueConfigurationBinaryObjectParser.BRACE_END);
        }
        
        if (enumKeyValueConfigurationBinaryObject.getData() != null && !enumKeyValueConfigurationBinaryObject.getData().isBlank()) {
            builder.append(enumKeyValueConfigurationBinaryObject.getData());
        }
        
        return builder.toString();
        /*
        int dataHash = 0;
        if (data != null) {
            dataHash = data.hashCode();
        }
        return "EnumKeyValueConfigurationBinaryObject [name=" + name + ", timestamp=" + timestamp + ", mimetype="
                + mimetype + ", dataHash=" + dataHash + "]";
                */
    }

    
    /**
     * Parse the content, e.g. {text/plain}VGV4dAo=, {text/plain}, VGV4dAo=
     *   
     * @param enumKeyValueConfigurationBinaryObject the {@link EnumKeyValueConfigurationBinaryObject}.
     * @param inputContent the content
     */
    private void parseContent(EnumKeyValueConfigurationBinaryObject enumKeyValueConfigurationBinaryObject, String inputContent) {
        if (inputContent == null || inputContent.isBlank()) {
            return;
        }

        String content = inputContent.trim();
        
        if (content.startsWith(BRACE_START)) {
            int idx = content.indexOf(BRACE_END);
            if (idx > 0) {
                enumKeyValueConfigurationBinaryObject.setMimetype(content.substring(1, idx).trim());
                
                if (content.length() > idx) {
                    content = content.substring(idx + 1).trim();
                } else {
                    content = null;
                }
            }
        }

        enumKeyValueConfigurationBinaryObject.setData(content);
    }


    /**
     * Parse the timestamp
     * 
     * @param inputTimestamp the timestamp
     * @return the timestamp or null if it is not a timestamp
     * @throws DateTimeParseException In case of an invalid date format
     */
    private Instant parseTimestamp(String inputTimestamp) throws DateTimeParseException {
        
        if (inputTimestamp == null) {
            return null;
        }
        
        // it could have date and time
        String[] timestampSplit = inputTimestamp.trim().split("T");
        if (timestampSplit.length == 0) {
            return null;
        }

        // check date
        if (!isNumber(timestampSplit[0].split("-"))) {
            return null;
        }

        // check time (hour, minute, seconds), ignore milliseconds and timezone
        if (timestampSplit.length > 1) {
            String[] simpleTime = timestampSplit[1].split("\\.");
            if (simpleTime != null && simpleTime.length > 0) {
                if (!isNumber(simpleTime[0].split(":"))) {
                    return null;
                }
            }
        }

        try {
            return DateUtil.getInstance().parseTimestamp(inputTimestamp);
        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * Check if it are just numbers
     *
     * @param values the string values
     * @return true if they are just numbers
     */
    private boolean isNumber(String[] values) {
        if (values == null || values.length == 0) {
            return false;
        }

        for (int i = 0; i < values.length; i++) {
            if (!isNumber(values[i])) {
                return false;
            }
        }

        return true;
    }

    
    /**
     * Check if it is just a number
     *
     * @param value the string value
     * @return true if it is just number
     */
    private boolean isNumber(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            Integer.valueOf(value.trim());
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
}
