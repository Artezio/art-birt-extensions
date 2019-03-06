# art-birt-extensions
Artezio BIRT engine &amp; studio extensions contains two eclipse features:
1. The Xlsx emitter. The emitter extends the standard BIRT xlsx emitter and allows to generate xlsx reports with excel comments.
2. The Union dataset feature patch. The feature extends the standard BIRT join dataset and provides ability to use a union type for joined tables. 

The features can be used in BIRT report designer and an application with integrated runtime-part of BIRT.

## Table of Contents
[1. Getting Started with a Report Designer](#1-getting-started-with-a-report-designer)

&nbsp;&nbsp;[1.1 Requirements](#11-requirements)

&nbsp;&nbsp;[1.2 Installation](#12-installation)

&nbsp;&nbsp;[1.3 How to Insert a Comment into a Cell of a Xlsx Report](#13-how-to-insert-a-comment-into-a-cell-of-a-xlsx-report)

&nbsp;&nbsp;[1.4 How to Create a Union Dataset](#14-how-to-create-a-union-dataset)


[2. Getting Started with a BIRT Runtime Application](#2-getting-started-with-a-birt-runtime-application)

&nbsp;&nbsp;[2.1 Requirements](#21-requirements)

&nbsp;&nbsp;[2.2 How to Get Project Artifacts](#22-how-to-get-project-artifacts)

&nbsp;&nbsp;[2.3 How to Configure Xlsx Emitter Options](#23-how-to-configure-xlsx-emitter-options)


[3. How To Include A Custom Eclipse Plugin Into The Repository](#3-how-to-include-a-custom-eclipse-plugin-into-the-repository)

&nbsp;&nbsp;[3.1 Eclipse Plugin Development](#31-eclipse-plugin-development)

&nbsp;&nbsp;[3.2 Preparing Eclipse Plugin to Package with Tycho](#32-preparing-eclipse-plugin-to-package-with-tycho)

&nbsp;&nbsp;[3.3 Eclipse Plugin Feature Development](#33-eclipse-plugin-feature-development)

&nbsp;&nbsp;[3.4 Adding Information About Your Eclipse Plugin To The Repository](#34-adding-information-about-your-eclipse-plugin-to-the-repository)

&nbsp;&nbsp;[3.5 Packaging And Installing](#35-packaging-and-installing)
  

## 1. Getting Started with a Report Designer
### 1.1 Requirements
Eclipse IDE with BIRT reporting tool. BIRT version 4.8.0 or higher. See [BIRT Downloads](http://download.eclipse.org/birt/downloads/build.php).

### 1.2 Installation
1.	Run Eclipse IDE.
2.	Click *Help > Install New Software....*
3.	Click on the *Add* button on the right.
4.	Type in a Name in the textbox. E.g., *art-birt-extensions*.
5.	Paste the following URL into the Location field: 
    https://github.com/Artezio/art-birt-extensions/raw/master/p2-repository
6.	Click *OK*.
7.	Check the *Group items by category* checkbox and select all items of *Artezio BIRT Extensions*.
8.	Click on *Next* and follow through the remaining dialogs to complete the installation.

### 1.3 How to Insert a Comment into a Cell of a Xlsx Report
A comment is added via a data report item and user properties.
1.	Select a data item.
2.	In the *Properties* tab select *User properties*.
3.	Click on the *Add* button.
4.	Type in *excel_comment* in the *Property Name* textbox.
5.	Select String as *Property Type*.
6.	Click *OK*.
7.	In the *Properties* tab select *Advanced*.
8.	Find *excel_comment* property and input any comment.
9.	To view the xlsx report with comments select *XLSX_COMMENTS* report type in the *View Report* menu.

**Please note.** If you need to add a dynamic comment, use *onCreate* script of a data item to set user property value. E.g.:
```
this.setUserPropertyValue("excel_comment",data["comment"]);
```

### 1.4 How to Create a Union Dataset

The Union dataset combines the result of two datasets. The result set includes all the rows that belong to all datasets.
1.	Open *Data Explorer* of the particular report.
2.	Right-click *Data Sets*, and choose *New Joint Data Set*.
3.	Select the first data set for the union at the left of *New Joint Data Set*. And select the second dataset at the right. **Notes** that the number and the order of the columns are taken from the first data set.
4.	Select a *Union* type, then choose *Finish* and click *OK*.

## 2. Getting Started with a BIRT Runtime Application
### 2.1 Requirements
1.	A Java-application with integrated BIRT Report Engine. BIRT version 4.8.0 or higher. See [how to integrate BIRT](http://www.eclipse.org/birt/documentation/integrating/reapi.php)
2.	To use Xlsx emmiter the following art-birt-extensions artifacts are required:
    ```
    com.artezio.birt.emitter.xlsx
    ```
3.	To use Union dataset the following art-birt-extensions artifacts are required:
    ```
    org.eclipse.birt.data
    org.eclipse.birt.report.data.adapter
    org.eclipse.birt.report.model
    ```
### 2.2 How to Get Project Artifacts
You can use the *JitPack* maven repository to get *art-birt-extensions* artifacts [![](https://jitpack.io/v/Artezio/art-birt-extensions.svg)](https://jitpack.io/#Artezio/art-birt-extensions)

E.g. if you want to use Xlsx emitter in your maven application you can add the *JitPack* repository to your build file:
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
And add the dependency:
```
<dependency>
  <groupId>com.github.Artezio.art-birt-extensions</groupId>
  <artifactId>com.artezio.birt.emitter.xlsx</artifactId>
  <version>1.0.1</version>
</dependency>
```
### 2.3 How to Configure Xlsx Emitter Options
To configure Xlsx emitter options you need to set correct *EmitterID* and *OutputFormat* in your application:
```
RenderOption renderOption = new EXCELRenderOption();
renderOption.setEmitterID("com.artezio.birt.emitters.XlsxEmitter");
renderOption.setOutputFormat("xlsx_comments");	
```

## 3. How To Include A Custom Eclipse Plugin Into The Repository
### 3.1 Eclipse Plugin Development
If you need information about eclipse plugin development see [Plugin development](https://www.ibm.com/developerworks/library/os-eclipse-plugindev1/index.html).

### 3.2 Preparing Eclipse Plugin to Package with Tycho
1. In *%art-birt-extensions-home%* create a directory that will include your eclipse plugin and eclipse plugin feature.
2. Add your eclipse plugin project to the directory.
3. Add *pom.xml* to eclipse plugin directory with the following information:

```
<parent>
    <groupId>com.github.Artezio.art-birt-extensions</groupId>
    <artifactId>birt-extensions</artifactId>
    <version>...</version>
    <relativePath>../../</relativePath>
</parent>

```

```
<packaging>eclipse-plugin</packaging>
```

```
<build>
    <plugins>
        <plugin>
            <groupId>org.eclipse.tycho</groupId>
            <artifactId>tycho-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.eclipse.tycho</groupId>
            <artifactId>target-platform-configuration</artifactId>
        </plugin>
    </plugins>
</build>
```
Pay attention information about version is omitted, you've to add actual parent version. Other info such as: artifactId, version etc. have to be provided by you.

### 3.3 Eclipse Plugin Feature Development
1. Open Eclipse.
2. *File -> New -> Other -> Plug-in Development -> Feature Project*.
3. On opened window specify necessary information.
4. Click *Next*.
4. Select your plugin(s).
5. Click *Finish*.
5. Add developed eclipse plugin feature into the directory created above.
6. Add *pom.xml* to feature project with the following information:
```
<parent>
    <groupId>com.github.Artezio.art-birt-extensions</groupId>
    <artifactId>birt-extensions</artifactId>
    <version>...</version>
    <relativePath>../../</relativePath>
</parent>
```

```
<packaging>eclipse-feature</packaging>
```

```
<build>
    <plugins>
        <plugin>
            <groupId>org.eclipse.tycho</groupId>
            <artifactId>tycho-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.eclipse.tycho</groupId>
            <artifactId>target-platform-configuration</artifactId>
        </plugin>
    </plugins>
</build
```
Pay attention information about version is omitted, you've to add actual parent version. Other info such as: artifactId, version etc. have to be provided by you.

### 3.4 Adding Information About Your Eclipse Plugin to The Repository
1. Open Eclipse.
2. *File -> Open Projects from File System -> %art-birt-extensions-home%/update-site*.
3. Double click on *site.xml* -> *Site Map* tab.
4. Add Feature.
5. Open *%art-birt-extensions-home%/pom.xml*.
6. Add to *modules* section the following:
```
<module>%art-birt-extensions-home%/your-created-directory/your-eclipse-plugin</module>   
<module>%art-birt-extensions-home%/your-created-directory/your-eclipse-plugin-feature</module>
```

### 3.5 Packaging And Installing
1. Go to %art-birt-extensions-home%.
2. `mvn clean install`.
3. Commit and push committed changes to remote repository.
4. Now your plugin is available using *JitPack* repository. For more information about fetching from the repository see [How to Get Project Artifacts](#22-how-to-get-project-artifacts).
