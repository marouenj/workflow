#!/usr/bin/python3

# template-engine.py --in_vals=/vagrant/in/vals.json --in_template=/vagrant/in/template --out=/vagrant/out

import argparse
import json
import re

# load args
parser = argparse.ArgumentParser()

parser.add_argument('--in_vals', dest='vals')
parser.add_argument('--in_template', dest='template')
parser.add_argument('--out', dest='out')

args = parser.parse_args()

# run a one-time regex to rule out the variable-free lines
matches = [re.findall(r'\$(\d+)', line) for line in open(args.template)]

# load the vals
vals = json.load(open(args.vals))

# parse file and inject vals
caseIdx = 0
for val in vals:
	lineIdx = 0
	out = open(args.out + '/gen-' + str(caseIdx), 'w')
	for line in open(args.template):
		if matches[lineIdx]:
			for val_idx in matches[lineIdx]:
				line = line.replace('$' + val_idx, val['Case'][int(val_idx)-1])
		out.write(line.strip() + '\n')
		# print (line.strip())
		lineIdx += 1
	out.close()
	caseIdx += 1
