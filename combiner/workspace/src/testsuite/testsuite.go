package testsuite


import (
	"container/list"
	"encoding/json"
	"fmt"
	"math"
	"os"

	"bitutil"
)


type TestTemplate struct {
	Prefix		string
	Suffix		string
	Name		string
	Vals		[]interface{}
}


type Testsuite struct {
	Num  float64
	Case []interface{}
}


func CastInterfaceToTestTemplate(vals []interface{}) []TestTemplate {
	if vals == nil {
		return nil
	}

	t_arr := make([]TestTemplate, len(vals))

	for key, val := range vals {
		var val2 = val.(map[string]interface{})

		var t TestTemplate
		switch val2["prefix"].(type) {
		case string:
			t.Prefix = val2["prefix"].(string)
		}
		switch val2["suffix"].(type) {
		case string:
			t.Suffix = val2["suffix"].(string)
		}
		switch val2["name"].(type) {
		case string:
			t.Name = val2["name"].(string)
		}
		t.Vals = val2["vals"].([]interface{})
		t_arr[key] = t
	}
	return t_arr
}


/*
func DumpVals(t []TestTemplate) {
	if t == nil {
		return;
	}

	for key, val := range t {
		fmt.Printf("%d prefix %s, suffix %s, name %s\n", key, val.Prefix, val.Suffix, val.Name)

		switch val.Vals[0].(type) {
		case map[string]interface{}:
			t_sub := CastInterfaceToTestTemplate(val.Vals)
			DumpVals(t_sub)
		default:
			fmt.Println(val.Vals)
		}
	}
}
*/


/*
func NumVars(t []TestTemplate) uint16 {
	if t == nil {
		return 0
	}

	var card uint16 = 0;

	for _, val := range t {
		switch val.Vals[0].(type) {
		case map[string]interface{}:
			t_sub := CastInterfaceToTestTemplate(val.Vals)
			card += NumVars(t_sub)
		default:
			card++;
		}
	}

	return card;
}
*/


// the number of vars to test, their respective vals, and the number of bits needed to represent these vals; all these data are scattered in a hierarchical data structure (json)
// this function flattens these data into two lists for an easy, repetitive processing later
// the two returned lists should have the same length
// the length denotes the number of vars
// index i refers to var i (order established by the in order traversal of the json structure)
// list 1 stores for each var i its respective vals
// list 2 stores for each var i the number of bits needed to represent the number of vals
func Flatten(t []TestTemplate) (*list.List, *list.List) {
	if t == nil {
		return nil, nil
	}

	vals := list.New()
	numBits := list.New()

	for _, val := range t {
		if val.Vals == nil || len(val.Vals) == 0 {
			continue;
		}

		switch val.Vals[0].(type) {
		case map[string]interface{}:
			sub_t := CastInterfaceToTestTemplate(val.Vals)
			v, b := Flatten(sub_t)
			vals.PushBackList(v)
			numBits.PushBackList(b)
		default:
			vals.PushBack(val.Vals)
			v := uint(len(val.Vals))
			numBits.PushBack(bitutil.BitsPerVal(v))
		}
	}

	return vals, numBits
}


func GoCaseByCase(vals *list.List, bits *list.List) []byte {
	if (vals == nil || bits == nil) {
		return nil
	}

	// count the total number of bits needed to represented all the cases
	// count the total number of effective case
	var bitNum uint = 0
	var caseNum int = 1
	for v, b := vals.Front(), bits.Front(); v != nil && b != nil; v, b = v.Next(), b.Next() {
		bitNum += b.Value.(uint)
		caseNum *= len(v.Value.([]interface{}))
	}

	var tcase []Testsuite = make([]Testsuite, caseNum)

	caseIdx := 0;
	for idx := 0; idx < int(math.Pow(2, float64(bitNum))); idx++ {
		caseVals := Filter(idx, vals, bits)
		if caseVals != nil {
			var t Testsuite
			t.Num = float64(caseIdx + 1)
			t.Case = caseVals

			tcase[caseIdx] = t
			caseIdx++;
		}
	}

	backToJson, err := json.Marshal(tcase)
	if err != nil {
		fmt.Println("error:", err)
        os.Exit(1)
	}

	return backToJson
}


func Filter(idx int, vals, bits *list.List) []interface{} {
	if (vals == nil || bits == nil) {
		return nil
	}

	var shifted uint = 0

	valsPerCase := make([]interface{}, vals.Len())

	for i, v, b := 0, vals.Front(), bits.Front(); v != nil && b != nil; v, b = v.Next(), b.Next() {
		valIdx := idx >> shifted
		mask := int(math.Pow(2, float64(b.Value.(uint)))-1)
		valIdx &= mask

		if valIdx >= len(v.Value.([]interface{})) {
			return nil
		}

		valsPerCase[i] = v.Value.([]interface{})[valIdx]
		i++;

		shifted += b.Value.(uint)
	}

	return valsPerCase
}
