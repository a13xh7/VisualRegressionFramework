## Visual regression framework: Java + TestNG + Selenium Webdriver + Selenide + aShot

##### Installation

1. Install java
2. Install maven 
3. Install browsers (chrome, firefox)

## Configuration
 
- `src/config/framework.properties` - configuration file 
  - `breakpoints=1920,768,360` -  breakpoints 
  - `allowableDiffSize=10` - allowable number of different pixels between two screenshots

- `src/main/java/framework/Config` class - configuration logic


**Default breakpoints**
        
- **Desktop**: breakpoint >= 1024px  
- **Tablet**:  breakpoint >= 768px AND breakpoint < 1024px
- **Mobile**:  breakpoint = < 768px

You can change breakpoints in `Report.getBreakpointType()` method.


## Screenshots comparison

##### Pages

- Compare page screenshots - all breakpoints
```java
Comparer.comparePages("test_name");
```

- Hide elements and compare page screenshots - all breakpoints
```java
Comparer.comparePages("test_name", new String[]{"igrored_element_css_locator", "igrored_element"});
```

- Compare page screenshots with specified breakpoint
```java
Comparer.comparePagesWithBreakpoint("test_name", "1920");
```

- Hide elements and compare page screenshots with specified breakpoint
```java
Comparer.comparePagesWithBreakpoint("test_name", "1920",  new String[]{"igrored_element"});
```

##### Elements

- Compare element screenshots - all breakpoints
```java
Comparer.compareElements("test_name", "css_locator");
```

- Compare element screenshots with specified breakpoint
```java
Comparer.compareElementsWithBreakpoint("test_name", "1920", "css_locator");
```

## Tests architecture

- `A_BeforeAllTests` - remove temporary screenshots, clear logs, init config before all tests
- `A_AfterAllTests` - generate report after all tests
- `A_ErrorsLogListener` - save errors for report
- `A_BaseTest` - all test classes must extend this class 
- `testng.xml` - add new test classes into this file, between `A_BeforeAllTests` and `A_AfterAllTests`

## Test execution

You can run tests using next command:

`mvn test -Dbrowser=chrome`

**parameters**
- `-Dbrowser=chrome` - browser. **chrome** or **firefox**.
- `-Dheadless=1` - **0** or **1**. run browser in headless mode
- `-Dclean=1` - **0** or **1**. remove expected screenshots and save actual ones as expected screenshots

All parameters are optional. Default values are set in `Config` class.

## Report

Report is generated in `A_AfterAllTests` class.
Report is located in `report` folder.
