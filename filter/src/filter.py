#!/usr/bin/python3

import argparse
import json
import re

from filter_util import *
from validate import *


# load args
parser = argparse.ArgumentParser()

parser.add_argument('--in_vals', dest='vals')
parser.add_argument('--in_filter', dest='filter')
parser.add_argument('--out', dest='out')

args = parser.parse_args()


# load the vals
vals = json.load(open(args.vals))
vals_is_valid(vals)

# load the filter
filter = json.load(open(args.filter))
filter_is_valid(filter)


lst_idx_2rm = []

for rule in filter:
  idx = 0
  for case in vals:
    if not keep(case['case'], rule['rule'], rule['action']):
      lst_idx_2rm.append(idx)
    idx += 1

lst_idx_2rm = sorted(list(set(lst_idx_2rm)), reverse=True)

for idx in lst_idx_2rm:
  del vals[idx]

json.dump(vals, open(args.out, 'w'))
