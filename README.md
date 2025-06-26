# lib-global-metadata
## A java library to read, write and edit unity's global-metadata.dat resource file.
* This is initial commit, i.e the project is on experimental stage.
* Tested with vesions 27 - 31.


### Example
<details><summary> <code><b>Java example</b></code></summary>

```java   
package com.test;

import com.reandroid.unity.metadata.GlobalMetadataFile;
import com.reandroid.unity.metadata.data.StringLiteralData;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.SectionStringData;
import com.reandroid.unity.metadata.value.ValueString;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ExampleGlobalMetadata {
    
    public static void main(String[] args) throws IOException {

        File file = new File("assets/bin/Data/Managed/Metadata/global-metadata.dat");

        // read binary file
        GlobalMetadataFile metadataFile = GlobalMetadataFile.read(file);

        // prints file version
        System.err.println("Version = " + metadataFile.getVersion());

        // prints sections offset and size info
        System.err.println(metadataFile.getHeader().toJson());

        // prints literal strings
        SectionStringData<StringLiteralData> sectionStringLiteralData = metadataFile.getLiteralStringsData();
        for (StringLiteralData data : sectionStringLiteralData) {
            System.err.println(data.get());
        }

        // prints type definitions
        MetadataSection<TypeDefinitionData> typesSection = metadataFile.getSection(MetadataSectionType.TYPE_DEFINITIONS);
        for (TypeDefinitionData data : typesSection) {
            System.err.println(data.getTypeName());
        }

        // Search and replace strings in literal strings

        String search_string = "play.google.com/log?action=SPY";
        String replace_string = "https://github.com/REAndroid/lib-global-metadata";
        
        for (StringLiteralData data : sectionStringLiteralData) {
            if (search_string.equals(data.get())) {
                data.set(replace_string);
                System.err.println("Replaced at: " + data.getIdx() + " (" + data.get() + ")");
            }
        }

        // Search and replace strings in attributes and default values

        Iterator<ValueString> iterator = metadataFile.visitValues(ValueString.class);
        while (iterator.hasNext()) {
            ValueString valueString = iterator.next();
            if (search_string.equals(valueString.get())) {
                valueString.set(replace_string);
            }
        }
        
        // refresh
        metadataFile.refresh();
        
        // write modified file
        File out = new File("modified_global-metadata.dat");
        metadataFile.write(out);
    }
}

```
</details>

### Contributing
To support this project you can:
* Open issues with full descriptions and samples.
* Make PR.


### Disclaimer
* This project is NOT intended for piracy and illegal actions.
* We are not responsible for any damages occurred while using this library.
* Before using this library, ensure that you are not violating UNITY's and others copyright.


<details> <summary><i><b>Contact</b></i></summary> 

* Telegram: [@kikfox](https://t.me/kikfox)
* Email: [thekikfox@gmail.com](mailto:thekikfox@gmail.com)

</details>


