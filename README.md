workflow
========


8 x 8 x 8 = 512 ...

If you got a chunk of code to test, with 3 variables, each having 8 possible values, then you've got a lot of test cases to cover. And you're far from a real case where you deal with more vars and vals.

Manually generating test cases is obviously not the right way to do things. This multi-purpose, multi-stage tool attempts to automate test-case generation. You can:
- generate all possible combinations of vals for a given set of vars
- marshal the vars (for each combination of vals) into objects/primitive types (Java, ...)
- align the vars into a csv, tsv (testing an insert query into Hive, ...)
- inject the vars into a custom file (XML template file, shell script, ...)

# Architecture

                  # # # # # ##                        # # # # ##                                 # # # # # # ##                                  # # # # # # # # # # #
                  #          #                        #        #                                 #            #                                  #                   #
	vars =======> # combiner # =======> vals =======> # filter # =======> vals.filtered =======> # formatter  # =======> vals.formatted =======> # code to be tested # =======> result verification (out of scope, for now ...)
                  #          #                        #        #                                 # (tsv, csv, #                                  #                   #
                  ## # # # # #                        ## # # # #                                 # java obj,  #                                  # # # # # # # # # # #
                                                                                                 # ...)       #
                                                                                                 # # # # # # ##

# Input vars.json
Input vars.json is the user's responsibility; It should contain the variables and their corresponding possible values. This should be enough when the intended test cases are to be formatted in, say, a tsv (to be input afterwards to a hive query for example). In other situations, where test cases are a set of Java objects, more information is needed (package/class namespace, setter, ...)

Here's the simplest form the file can take:

	[
		{
			"name" : "fname",
			"vals" : [ "John", "Jon", "Joh", "" ]
		},
		{
			"name" : "lname",
			"vals" : [ "Doe", "Do", "" ]
		},
		{
			"name" : "sex",
			"vals" : [ "Male", "Mle", "Female", "" ]
		},
		{
			"name" : "age",
			"vals" : [ 10, -1, 1000 ]
		}
	]

Above, three vars are considered, each with a set of possible vals. The example below is more sophisticated and it describes test input for Java program/method:

	[
		{
			"prefix" : "com",
			"suffix" : "Person",
			"vals" : [
				{
					"name" : "fname",
					"setter" : "setFname",
					"vals" : [ "John", "Jon", "" ]
				},
				{
					"name" : "lname",
					"setter" : "setLname",
					"vals" : [ "Doe", "Do", "" ]
				},
				{
					"prefix" : "java.util",
					"suffix" : "Date",
					"name" : "birthdate",
					"setter" : "setBirthdate",
					"vals" : [
						{
							"name" : "year",
							"type" : "int",
							"setter" : "setYear",
							"vals" : [ 2014 ]
						},
						{
							"name" : "month",
							"type" : "int",
							"setter" : "setMonth",
							"vals" : [ 0, 6, 13 ]
						},
						{
							"name" : "day",
							"type" : "int",
							"setter" : "setDate",
							"vals" : [ 0, 1, 31, 1000 ]
						}
					]
				}
			]
		},
		{
			"prefix" : "com",
			"suffix" : "Company",
			"vals" : [
				{
					"name" : "name",
					"setter" : "setName",
					"vals" : [ "Kampani", "Kampni" ]
				},
				{
					"name" : "hq",
					"setter" : "setHq",
					"vals" : [ "Tokyo", "Tkyo", "" ]
				}
			]
		}
	]

Here prefix and suffix are general terms whose definition depends on the language we're generating test cases for. For Java, prefix and suffix refers to the package and class names respectively.

# Combiner
https://github.com/marouenj/workflow/blob/master/combiner/README.md

# Filter
https://github.com/marouenj/workflow/blob/master/filter/README.md

# Formatters
https://github.com/marouenj/workflow/blob/master/formatters/README.md
