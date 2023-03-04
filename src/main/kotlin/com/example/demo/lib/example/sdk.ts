import UserTypes from "./UserTypes";
import { ServerResponse } from "./ServerResponse";
import { replacePathAndQueryVariables } from "./Utils";

export default function BuildSDK(apiBaseUrl: string) { return {
	Users: {
		second: async (args: { 
	params: {	a: string,
	b: string},
	query: {	search?: string,
	orderBy: boolean},
	body: UserTypes.RequestData,
 }): Promise<ServerResponse<UserTypes.ResponseData, UserTypes.ErrorType>> => 
	fetch(
		replacePathAndQueryVariables(`${apiBaseUrl}/api/users/{a}/one/{b}/two`, args.params, args.query),
		{
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(args.body)
		}
	)
		.then(res => res.json())
,
	},
}}
