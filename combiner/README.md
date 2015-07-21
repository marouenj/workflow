Combiner
========


# Command

	combiner -in_vars=/vagrant/vagrant-init/sample/combiner/vars.json -out_vals=/tmp/vals.json

# What is does

Combiner's sole responsibility is to match together the different combinations of the vals.

# Input: vars.json

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

# Output: vals.json

	[
		{
			"Num" : 1,
			"Case" : ["John","Doe",2014,0,0,"Kampani","Tokyo"]
		},
		{
			"Num" : 2,
			"Case" : ["Jon","Doe",2014,0,0,"Kampani","Tokyo"]
		},
		{
			"Num" : 3,
			"Case" : ["John","Do",2014,0,0,"Kampani","Tokyo"]
		},

		.
		.
		.

		{
			"Num" : 430,
			"Case" : ["Jon","Do",2014,13,1000,"Kampni",""]
		},
		{
			"Num" : 431,
			"Case" : ["John","",2014,13,1000,"Kampni",""]
		},
		{
			"Num" : 432,
			"Case" : ["Jon","",2014,13,1000,"Kampni",""]
		}
	]
