import BuildSDK from "./api-sdk/sdk";

const sdk = BuildSDK("http://localhost:8080");

sdk.Users.second({
  body: {
    data: [1, 2, 3, 4, 5],
  },
  params: {
    b: "This is b",
    a: "This is a",
  },
  query: {
    orderBy: false,
    search: "This is search",
  },
})
  .then((res) => {
    if (res.isSuccess) {
      const data = res.data;
      console.log("SUCCESS", data.date);
    } else {
      const error = res.error;
      console.log("ERROR", error);
    }
  });
