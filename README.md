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

  
[2. Getting Started with a BIRT Runtime Application](@2-getting-started-with-a-birt-runtime-application)

&nbsp;&nbsp;[2.1 Requirements](21-requirements)  

&nbsp;&nbsp;[2.2 How to Get Project Artifacts](22-how-to-get-project-artifacts)  

&nbsp;&nbsp;[2.3 How to Configure Xlsx Emitter Options](23-how-to-configure-xlsx-emitter-options)
  

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

