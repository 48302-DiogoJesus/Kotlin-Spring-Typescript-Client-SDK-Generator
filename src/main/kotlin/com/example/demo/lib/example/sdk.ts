import { RequestData, ResponseData } from "./types";
import { replacePathAndQueryVariables } from "./utils";

const sdk = {
  Users: {
    getById: async (args: {
      params: { id: string };
    }): Promise<void> => {
      await fetch(
        replacePathAndQueryVariables(
          "HARDCODED_BASE_URL/api/users/{id}",
          args.params,
          undefined,
        ),
      );
    },
    create: async (args: {
      params: { id: string };
      query: { search: string; orderBy: boolean };
      body: RequestData;
    }): Promise<ResponseData> => {
      return fetch(
        replacePathAndQueryVariables(
          "HARDCODED_BASE_URL/api/users/",
          args.params,
          args.query,
        ),
        {
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(args.body),
        },
      )
        .then((res) => res.json());
    },
  },
};

export default sdk;
