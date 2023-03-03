[ ] Base URL needs to come from somewhere
[ ] Write tests
[ ] Add support for ResponseEntity<ResponseBody | ErrorResponseBody> in kotlin and then a converter to TS (in the
types.d.ts file and used as return type for the promises)
[ ] Look for edge cases

[x] Optional types not working

- @RequestParam(required = false)
- @PathVariable(required = false)
  [x] Refactor the recursive code fraction

EDGE CASES (maybe in nested types, special kotlin data types, test Timestamp and Date to TS Date, requestbody that is a
map or a data class with a property that is a map, request body being primitive):
