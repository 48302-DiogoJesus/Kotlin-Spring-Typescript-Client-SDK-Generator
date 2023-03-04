type SuccessResponse<S> = {
  isSuccess: true;
  data: S;
};

type ErrorResponse<E> = {
  isSuccess: false;
  error: E;
};

export type ServerResponse<S, E> = SuccessResponse<S> | ErrorResponse<E>;
