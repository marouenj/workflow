Formatters
==========


# What they do

Formatters are utilities that act on the combined vals in different ways.

If it is about Java vars, then the formatter of choice should be a 'java-marshaller' that serializes and persists the vars to disk (file). The insertion to file follows a FIFO order, test case-wise, so that inputting the vars to your code under test is streamlined.

If it's about a set of vars that make up elements in an XML file, then the formatter to work with ('template-engine') generates as many XML files as there are combinations from a base XML template.
