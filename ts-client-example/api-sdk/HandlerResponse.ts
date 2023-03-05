type SuccessResponse<S> = {
  error: null;
  data: S;
};

type ErrorResponse<E> = {
  data: null;
  error: E;
};

export type HandlerResponse<S, E> = SuccessResponse<S> | ErrorResponse<E>;