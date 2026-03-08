                                                                                        package com.wtmsbackend.services;

                                                                                        import com.wtmsbackend.dto.request.ForgetPasswordRequest;
                                                                                        import com.wtmsbackend.dto.request.LoginRequest;
                                                                                        import com.wtmsbackend.dto.request.UserRequest;
                                                                                        import com.wtmsbackend.dto.response.LoginResponse;
                                                                                        import com.wtmsbackend.dto.response.UserResponse;
                                                                                        import jakarta.validation.Valid;
                                                                                        import jakarta.validation.constraints.Email;
                                                                                        import jakarta.validation.constraints.NotBlank;

                                                                                        public interface AuthService {
                                                                                            UserResponse register(UserRequest userRequest);
                                                                                            LoginResponse login(LoginRequest request);

                                                                                            void verifyOtpCode(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email, @NotBlank(message = "OTP Code is required") String otpCode);

                                                                                            void forgetPassword(@NotBlank @Email String email, @Valid ForgetPasswordRequest request);

                                                                                            void resendCode(@NotBlank @Email String email);
                                                                                        }