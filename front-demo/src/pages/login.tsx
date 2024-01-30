import LoginForm from "@/components/pages/login/LoginForm";
import axios from "axios";
import { useRouter } from "next/router";
import { FormEventHandler, useEffect } from "react";

const LoginPage = () => {
  const router = useRouter();
  const apiHost = process.env.NEXT_PUBLIC_API_HOST;

  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios(`${apiHost}/api/v1/userinfo`, {
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
  }, [apiHost, router]);

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
      .post(`${apiHost}/api/v1/login`, data, {
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
      });
  };

  return (
    <>
      <h1>Login</h1>
      <LoginForm handleLogin={handleLogin} />
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default LoginPage;
