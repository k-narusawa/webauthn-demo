import axios from "axios";
import { useRouter } from "next/router";
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

  return (
    <>
      <h1>Home</h1>
      <label>Username: {userinfo?.username}</label>
    </>
  );
};

export async function getStaticProps() {
  return {
    props: {},
  };
}

export default HomePage;
