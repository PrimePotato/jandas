# Jandas

#### Java data and analytics toolkit 

Bringing the Data Frame concept to Java. It aims to provide features that will be familiar those who have used data frames in Pandas and R.

### Main Features

 * Mixed type storage for optimal memory usage
 * Pretty print
 * Column operations
 * Linear Algebra via EJML   
 * Parsing from / writing To CSV
 * Group by
 * Joining and merging
  

### Installation
The source code is hosted on GitHub at: https://github.com/PrimePotato/jandas.

It is also available via Maven Central and so the pom.xml should include:

    <dependency>
      <groupId>io.github.primepotato</groupId>
      <artifactId>jandas</artifactId>
      <version>0.1</version>
    </dependency>


### Usage

##### Manual Construction

Here is a simple example of a data frame being manually constructed:  

    String[] str1 = {"a", "b", "c"};
    StringColumn sc1 = new StringColumn("str", true, str1);

    int[] data1 = {1, 2, 3};
    IntegerColumn ic1 = new IntegerColumn("int1", true, data1);

    int[] data2 = {1, 2, 3};
    IntegerColumn ic2 = new IntegerColumn("int2", true, data2);

    double[] dblData = {1., 2., 3.};
    DoubleColumn dbl1 = new DoubleColumn("dbl1", true, dblData);

    List<Column> cols = new ArrayList<>();
    cols.add(sc1);
    cols.add(ic1);
    cols.add(ic2);
    cols.add(dbl1);

    df1 = new DataFrame("1", cols);
    
       
##### Read CSV and To Csv

It is possible to load data from CSV and also save data frame in 

    dataFrame = Jandas.readCsv("src/test/resources/biostats.csv");

Write to csv:

    dataFrame.toCsv("example.csv");


##### Column Operations


### Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. Full guidance can be found in [Contribution guidelines for this project](CONTRIBUTING.md). 





   
  
 