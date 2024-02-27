import Readme from "./pages/Readme.jsx";
import Users from "./pages/Users.jsx";

export const routes = [
    {
        path: "/readme",
        element: <Readme/>
    },
    {
        path: "/",
        element: <Users/>
    }
]