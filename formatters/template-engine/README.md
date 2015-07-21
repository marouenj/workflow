From-template
=============


# Command

	mkdir /tmp/out
	template-engine.py --in_vals=/vagrant/vagrant-init/sample/formatters/template-engine/vals.json --in_template=/vagrant/vagrant-init/sample/formatters/template-engine/template --out=/tmp/out

Given the combined vals and a template, this utility generates templates with each combination of vals injected in a separate copy of the template.

Here's a use case that explains the relevance of this tool. Say we have a Redis cluster with a master, 2 slaves and three sentinels. We want to test the availability of the cluter under every possible scenario (one slave and one sentinel already already down when the master goes down, ...). The cluster is deemed available if failover occurs within s seconds.

A first step in automation is to generate the scripts for each test case from a pre-defined template

# Input: template

	######
	# up #
	######

	./start.master.sh

	./start.slave1.sh
	./start.slave2.sh

	./start.sentinel1.sh
	./start.sentinel2.sh
	./start.sentinel3.sh

	########
	# kill #
	########

	$1./kill.slave1.sh
	$2./kill.slave2.sh

	$3./kill.sentinel1.sh
	$4./kill.sentinel2.sh
	$5./kill.sentinel3.sh

Notice the $1, $2, ...

Theese are the markers where the vals are to be injected. The idea here is to put either '#' or '' depending on the combination.

# vars.json

	[
		{
			"name" : "slave1",
			"vals" : [ "", "#"]
		},
		{
			"name" : "slave2",
			"vals" : [ "", "#"]
		},
		{
			"name" : "sentinel1",
			"vals" : [ "", "#"]
		},
		{
			"name" : "sentinel2",
			"vals" : [ "", "#"]
		},
		{
			"name" : "sentinel3",
			"vals" : [ "", "#"]
		}
	]

# Input: vals.json

	[
		{
			"Num" : 1,
			"Case" : ["","","","",""]
		},
		{
			"Num" : 2,
			"Case" : ["#","","","",""]
		},
		{
			"Num" : 3,
			"Case" : ["","#","","",""]
		},

		.
		.
		.

		{
			"Num" : 30,
			"Case" : ["#","","#","#","#"]
		},
		{
			"Num" : 31,
			"Case" : ["","#","#","#","#"]
		},
		{
			"Num" : 32,
			"Case" : ["#","#","#","#","#"]
		}
	]
