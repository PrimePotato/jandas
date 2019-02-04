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
  

### Where to get it
The source code is hosted on GitHub at: https://github.com/PrimePotato/jandas.

It is also available via Maven Central and so the pom.xml should include:

    <dependency>
      <groupId>io.github.primepotato</groupId>
      <artifactId>jandas</artifactId>
      <version>0.1</version>
    </dependency>


### Getting Started

##### Manual Construction

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
       
##### Read CSV and Print

    dataFrame = Jandas.readCsv("src/test/resources/EG1.csv");
    dataFrame.print(20);

### Detailed Examples

More detailed demonstrations of the code can be found in _/src/test/java/io/github/primepotato/jandas/demos_.
   
  
 