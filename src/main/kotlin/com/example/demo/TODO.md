EDGE CASES (test Timestamp and Date to TS Date, requestbody that is a
map or a data class with a property that is a map, request body being primitive):

[ ] Look for edge cases

[x] Write basic tests
[x] Add support for ResponseEntity<ResponseBody | ErrorResponseBody> in kotlin and then a converter to TS (in the
types.d.ts file and used as return type for the promises)
[x] Base URL needs to come from somewhere
[x] Optional types not working

- @RequestParam(required = false)
- @PathVariable(required = false)
  [x] Refactor the recursive code fraction

## Spring Handlers must follow a return type convention:

- SPECIFY HANDLER RETURN TYPE => ResponseEntity<HandlerResponse<ResponseData, ErrorType>>
- RETURN ERROR => return HandlerResponse.error(ErrorType(message = "asd"), 400)
- RETURN SUCCESS => return HandlerResponse.success(ResponseData(output = true))
