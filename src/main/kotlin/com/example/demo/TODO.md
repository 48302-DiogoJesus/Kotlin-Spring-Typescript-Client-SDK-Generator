[ ] Run a TS formatter through the output files

[ ] Write README.md saying what this is
[ ] Write usage instructions
[ ] Build jar and try it
[ ] Wrapper around fetch api to have success and error callbacks

[x] complete system test with Timestamp to Date DIRECTLY
[x] Increase spring example's complexity
[x] Could optimize and keep mutating the handlers metadata as they pass though the steps
[x] Look for some edge cases
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
