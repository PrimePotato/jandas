# Jandas

[![Build Status](https://travis-ci.org/PrimePotato/jandas.svg?branch=master)](https://travis-ci.org/PrimePotato/jandas)

[![codecov](https://codecov.io/gh/PrimePotato/jandas/branch/master/graph/badge.svg)](https://codecov.io/gh/PrimePotato/jandas)


### Java Data Frames 

Bringing the Data Frame concept to Java. It aims to provide features that will be familiar those who have used data frames in Pandas and R.

### Main Features

 * Mixed type storage for optimal memory usage
 * Pretty printing
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


#### Manual Construction

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

#### Read & Write to CSV 

It is possible to load data from CSV and also save data frame in 

    dataFrame = Jandas.readCsv("src/test/resources/biostats.csv");

Write to csv:

    dataFrame.toCsv("example.csv");

                                                                     
#### Printing    
                                                                                             
Here is an example using the sample csv data                                                                             
                                                                                                                
    dataFrame = Jandas.readCsv("src/test/resources/biostats.csv");
    dataFrame.print();
                                                                                                                
output:                                                                                                         
                                                                                                                
        Sex  |  Weight (Sep)  |  Weight (Apr)  |  BMI (Sep)  |  BMI (Apr)  |
       ---------------------------------------------------------------------
          M  |            72  |            59  |      22.02  |      18.14  |
          M  |            97  |            86  |       19.7  |      17.44  |
          M  |            74  |            69  |      24.09  |      22.43  |
          M  |            93  |            88  |      26.97  |      25.57  |
          F  |            68  |            64  |      21.51  |       20.1  |
          M  |            59  |            55  |      18.69  |       17.4  |
          F  |            64  |            60  |      24.24  |      22.88  |
          F  |            56  |            53  |      21.23  |      20.23  |
          F  |            70  |            68  |      30.26  |      29.24  |
          F  |            58  |            56  |      21.88  |      21.02  |
        ...  |           ...  |           ...  |        ...  |        ...  |
          M  |            66  |            71  |       21.4  |      22.97  |
          F  |            52  |            57  |      17.72  |      19.42  |
          M  |            71  |            77  |      22.26  |      23.87  |
          F  |            55  |            60  |      21.64  |      23.81  |
          M  |            65  |            71  |      22.51  |      24.45  |
          M  |            75  |            82  |      23.69  |       25.8  |
          F  |            42  |            49  |      15.08  |      17.74  |
          M  |            74  |            82  |      22.64  |      25.33  |
          M  |            94  |           105  |      36.57  |      40.86  |

#### Column Operations

Operators such as addition, multiplication, etc are supported on floating point columns. The full details can be found 
here [EJML](http://ejml.org).
 
##### Basic Operation

It is possible to perform operations on columns via the EJML library. For example 

    dfFreshmen = Jandas.readCsv("src/test/resources/freshman_kgs.csv");
    DoubleColumn dc = dfFreshmen.column("BMI (Sep)")
    System.out.println(ask.scale(2));    

  
##### Equations

Also leveraging the EJML library it is possible to create columns from equations:

    DoubleColumn a = dfFreshmen.column("BMI (Sep)");
    DoubleColumn b = dfFreshmen.column("BMI (Apr)");
    DoubleColumn c = new DoubleColumn("Mid", false, new double[0]);

    Equation eq = new Equation();
    eq.alias(a.getMatrix(), "a", b.getMatrix(), "b", c.getMatrix(), "c");
    eq.process("c = (a+b)/2");

_Note: that this currently only works on columns with a double data type_  

Or more simply:
   
    dfFreshmen.createColumn("avg", "(BMISep+BMIApr)/2.0");
    
    
### Grouping & Aggregation

 m


### Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. 
Full guidance can be found in [Contribution guidelines for this project](./contributing.md). 





   
  
 