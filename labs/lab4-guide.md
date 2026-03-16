# **Lab Guide: Intro to Reflection**

**Objectives:**

* Understand the basics of reflection in Kotlin and Java.
* Practice using reflection to inspect classes, methods, and properties.
* Explore the differences in reflection capabilities between Kotlin and Java.

---

## **Table of Contents**

1. [Setup](#part-1-setup)
2. [Exploring Reflection in Java](#part-2-exploring-reflection-in-java)
3. [Exploring Reflection in Kotlin](#part-3-exploring-reflection-in-kotlin)

## Part 1:  Setup

1. In your local folder for this course labs, create a Gradle multi-module
   project by running the following command in the terminal: `gradle init`

2. Press Enter to select the default options for all prompts, except for the
   implementation language, which must be set to **Kotlin** instead of Java. The
   selected options should be:

* Type of build to generate: Application
* Implementation language: Kotlin
* Target Java version: 21
* Project name: `<default name or your choice>`
* Application structure: Single application project
* Build script DSL: Kotlin
* Test framework: kotlin.test
* Generate build using new APIs and behavior: No

3. Rename the newly created project folder `app` to `lab-4` and update
   the `settings.gradle.kts` to include the new project name accordingly.

4. Run the command `./gradlew build` to ensure the project is set up correctly
   and builds without errors.

## Part 2: Exploring Reflection in Java

Despite you are using the **Java Reflection API**, you should implement the required
functionality in **Kotlin**, including the unit tests.

1. Implement the extension function `Any.fieldsToJson(): String`, which converts
   the values of declared **fields** of a given object to JSON. This function should
   use the **Java Reflection API** to inspect fields.

2. Implement a unit test that validates the functionality of the
   `fieldsToJson()` function for a domain object with only `String` and
   **primitive** types as properties. The test should create an instance of the domain
   object, call the `fieldsToJson()` function, and assert that the resulting
   JSON string correctly represents the field names and their corresponding
   values.
   
3. Enhance the `fieldsToJson()` function to handle non-primitive types, also
   including iterables (i.e. `Iterable<T>`).  
   The function should be able to convert the fields of these non-primitive
   types into JSON as well, by recursively inspecting their fields and values. 
   For iterables, the function should convert each element to JSON and include
   it in the resulting JSON string.  
   For other reference types, it should recursively call `fieldsToJson()` on the
   instance to convert its fields to JSON.
  
4. Implement a unit test that validates the enhanced functionality of the
   `fieldsToJson()` function for a domain object that contains non-primitive types
   as fields, including iterables and other reference types.

## Part 3: Exploring Reflection in Kotlin

Implement the extension function `membersToJson` using the Kotlin Reflection
API.  
This function encodes properties and non-void parameterless functions.  

Follow the same steps as in Part 2 to implement and test the `membersToJson()`
function, ensuring that it correctly converts the properties and parameterless
functions of a given object into JSON format, for both primitive and
non-primitive types, including iterables.
