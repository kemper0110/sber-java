import Readme from "./pages/Readme.jsx";
import Users from "./pages/Users.jsx";

export const routes = [
    {
        path: "/",
        element: <Readme/>
    },
    {
        path: "/users",
        element: <Users/>
    }
]