Filter
======


# Command

	filter.py --in_vals=/vagrant/vagrant-init/sample/filter/vals.json --in_filter=/vagrant/vagrant-init/sample/filter/filter.json --out=/tmp/vals.filtered.json

# What is does

Sometimes, some combinations may be out of the testing scope. Sometimes, they don't make sense at all. The purpose of this stage is to filter out the unwanted test cases following a set of rules specified by the user.

For each rule, a subset of the vals needs to be explicitly specified. For the rest of the vals, a wildcard is put. If a rule matches a test case, an action ('keep', 'discard') is triggered.

# Example

Consider the following vars.json

	[
		{
			"prefix" : "com",
			"suffix" : "Person",
			"vals" : [
				{
					"name" : "fname",
					"setter" : "setFname",
					"vals" : [ "John", "Jane", "" ]
				},
				{
					"name" : "lname",
					"setter" : "setLname",
					"vals" : [ "Doe", "" ]
				},
				{
					"name" : "sex",
					"setter" : "setSex",
					"vals" : [ "Male", "Female", "" ]
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

If, for some reason, any combination that couples "John" with "Female" or "Jane" with "Male" is out of the testing scope, then it is better be discarded programatically.

# Input: vars.json

	[
		{
			"num" : 1,
			"case" : ["John","Doe","Male","Kampani","Tokyo"]
		},
		{
			"num" : 2,
			"case" : ["Jane","Doe","Male","Kampani","Tokyo"]
		},

		.
		.
		.

		{
			"num" : 107,
			"case" : ["Jane","","","Kampni",""]
		},
		{
			"num" : 108,
			"case" : ["","","","Kampni",""]
		}
	]

# Input: filter.json

	[
		{
			"action" : "discard",
			"rule" : [ "John", "*", "Female", "*", "*"]
		},
		{
			"action" : "discard",
			"rule" : [ "Jane", "*", "Male", "*", "*" ]
		}
	]

"action" can take either "discard" or "keep". The rules are applied following their order of appearance.
