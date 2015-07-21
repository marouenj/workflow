package main


import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"os"

	"testsuite"
)


func main() {
	inPtr := flag.String("in_vars", "./vars.json", "input")
	outPtr := flag.String("out_vals", "./vals.json", "output")
	flag.Parse()

	file, err := ioutil.ReadFile(*inPtr)
	if err != nil {
        fmt.Printf("File error: %v\n", err)
        os.Exit(1)
    }

	var ttemp []testsuite.TestTemplate

	err2 := json.Unmarshal(file, &ttemp)
	if err2 != nil {
		fmt.Println("error:", err2)
        os.Exit(1)
	}

	// testsuite.DumpVals(ttemp)

	// fmt.Println(testsuite.NumVars(ttemp))

	vals, bits := testsuite.Flatten(ttemp)

	// for v, b := vals.Front(), bits.Front(); v != nil && b != nil; v, b = v.Next(), b.Next() {
	// 	fmt.Println(v.Value)
	// 	fmt.Println(b.Value)
	// }
	// fmt.Println()

	var tcase []byte = testsuite.GoCaseByCase(vals, bits)

	err3 := ioutil.WriteFile(*outPtr, tcase, 0666)
	if err3 != nil {
        fmt.Printf("File error: %v\n", err3)
        os.Exit(1)
    }
}
