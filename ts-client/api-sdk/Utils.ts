export function replacePathAndQueryVariables(
  url: string,
  variables?: { [key: string]: any },
  query?: { [key: string]: any },
): string {
  let result = url;
  if (variables) {
    for (const key in variables) {
      if (Object.prototype.hasOwnProperty.call(variables, key)) {
        result = result.replace(`{${key}}`, variables[key]);
      }
    }
  }

  if (query) {
    result += "?";
    Object.entries(query).forEach(([k, v]) => {
      result += `${k}=${v}&`;
    });
  }

  return result;
}