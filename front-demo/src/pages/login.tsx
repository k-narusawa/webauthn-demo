import LoginForm from "@/components/pages/login/LoginForm";
import { useWebAuthn } from "@/hooks/userWebAuthn";
import axios from "axios";
import { useRouter } from "next/router";
import { FormEventHandler, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const LoginPage = () => {
  const router = useRouter();
  const { getAuthOptions, postResults } = useWebAuthn();
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;

  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios(`${apiHost}/v1/userinfo`, {
        withCredentials: true,
      })
        .then(() => {
          router.push("/");
        })
        .catch(() => {
          console.log("Not logged in");
        });
    };
    fetchUserInfo();
  }, []);

  const handleLogin: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const formData = new FormData(e.currentTarget);
    const username = formData.get("username") as string;
    const password = formData.get("password") as string;

    const data = {
      username: username,
      password: password,
    };

    await axios
      .post(`${apiHost}/v1/login`, data, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true,
      })
      .then(function (response) {
        console.log(response.data);
        router.push("/");
      })
      .catch(function (error) {
        console.log(error.response.data);
        toast.error("Invalid username or password");
      });
  };

  const handleWebAuthnLogin: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const options = await axios(`${apiHost}/v1/webauthn/authentication/options`, {
      withCredentials: true,
    })
      .then(function (response) {
        return response.data;
      })
      .catch(function (error) {
        console.log(error.response.data);
      });

    const credentials = await getAuthOptions(options);
    await postResults(options.challenge, credentials);
  };

  return (
    <>
      <div className="pt-10">
        <ToastContainer />
        <LoginForm
          handleLogin={handleLogin}
          handleWebAuthnLogin={handleWebAuthnLogin}
        />
      </div>
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default LoginPage;
