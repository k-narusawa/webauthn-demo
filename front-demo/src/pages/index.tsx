import axios from "axios";
import { useRouter } from "next/router";
import { destroyCookie, parseCookies } from "nookies";
import { useEffect, useState } from "react";

const HomePage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const router = useRouter();

  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios("http://127.0.0.1:8080/api/v1/userinfo", {
        withCredentials: true,
      })
        .then((response) => {
          setUserInfo(response.data);
        })
        .catch((error) => {
          console.log(error.response.data);
          router.push("/login");
        });
    };
    fetchUserInfo();
  }, [router]);

  const handleLogout = async () => {
    await axios("http://127.0.0.1:8080/api/v1/logout", {
      method: "POST",
      withCredentials: true,
    })
      .then(function (response) {
        console.log(response.data);
        router.push("/login");
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  };

  return (
    <>
      <h1>Home</h1>
      <label>Username: {userinfo?.username}</label>
      <br />
      <button
        onClick={handleLogout}
        type="button"
        className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
      >
        Logout
      </button>
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default HomePage;
