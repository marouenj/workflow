def matches (case, rule):
  match = 1
  for i in range(len(case)):
    if rule[i] != "*" and case[i] != rule[i]:
      match = 0
      break
  return match



def keep (case, rule, action):
  keep = 0
  if action == "keep":
    keep = 1 if matches(case, rule) else 0
  else:
    keep = 0 if matches(case, rule) else 1
  return keep
