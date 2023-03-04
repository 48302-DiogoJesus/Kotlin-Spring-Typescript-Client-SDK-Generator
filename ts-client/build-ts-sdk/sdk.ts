import UserTypes from "./UserTypes";
import { ServerResponse } from "./ServerResponse";
import { replacePathAndQueryVariables } from "./Utils";

export default function BuildSDK(apiBaseUrl: string) { return {
	Users: {
		second: (args: { 
	params: {	a: string,
	b: string},
	query: {	search?: string,
	orderBy: boolean},
	body: UserTypes.RequestData,
 }): Promise<ServerResponse<UserTypes.ResponseData, string>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/{a}/one/{b}/two`, args.params, args.query),
		{
			method: "POST",
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(args.body)
		}
	)
		.then(res => res.json())
,
	},
}}
