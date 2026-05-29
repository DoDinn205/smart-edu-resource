import { useReducer } from "react";
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import cookies from "react-cookies";

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

import { MyUserContext } from "./configs/Context";
import MyUserReducer from "./reducers/MyUserReducer";

import Header from "./components/common/Header";
import Footer from "./components/common/Footer";

import Home from "./screens/Home/Home";
import Login from "./screens/auth/Login";
import StudentRegister from "./screens/auth/StudentRegister";
import LecturerRegister from "./screens/auth/LecturerRegister";
import ForgotPassword from "./screens/auth/ForgotPassword";
import ResetPassword from "./screens/auth/ResetPassword";
import ResourceBrowse from "./screens/Resource/ResourceBrowse";
import ResourceDetail from "./screens/Resource/ResourceDetail";
import CourseBrowse from "./screens/Course/CourseBrowse";
import CourseDetail from "./screens/Course/CourseDetail";
import CourseLearn from "./screens/Course/CourseLearn";
import StudentDashboard from "./screens/Student/StudentDashboard";
import MyCourses from "./screens/Student/MyCourses";
import StudentProfile from "./screens/Student/StudentProfile";
import LearningPath from "./screens/Student/LearningPath";
import QuizList from "./screens/Quiz/QuizList";
import QuizTaking from "./screens/Quiz/QuizTaking";
import QuizResult from "./screens/Quiz/QuizResult";
import Forum from "./screens/Forum/Forum";
import ForumThread from "./screens/Forum/ForumThread";
import Chat from "./screens/Chat/Chat";
import PaymentHistory from "./screens/Payment/PaymentHistory";

const AppLayout = () => {
    const { pathname } = useLocation();
    const isFullscreen = /^\/courses\/\d+\/learn$/.test(pathname);

    return (
        <div className={isFullscreen ? "" : "d-flex flex-column min-vh-100"}>
            {!isFullscreen && <Header />}
            <main className={isFullscreen ? "" : "flex-grow-1"}>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register/student" element={<StudentRegister />} />
                    <Route path="/register/lecturer" element={<LecturerRegister />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />
                    <Route path="/reset-password" element={<ResetPassword />} />
                    <Route path="/resources" element={<ResourceBrowse />} />
                    <Route path="/resources/:id" element={<ResourceDetail />} />
                    <Route path="/courses" element={<CourseBrowse />} />
                    <Route path="/courses/:id" element={<CourseDetail />} />
                    <Route path="/courses/:id/learn" element={<CourseLearn />} />
                    <Route path="/student/dashboard" element={<StudentDashboard />} />
                    <Route path="/my-courses" element={<MyCourses />} />
                    <Route path="/profile" element={<StudentProfile />} />
                    <Route path="/learning-path" element={<LearningPath />} />
                    <Route path="/quizzes" element={<QuizList />} />
                    <Route path="/quizzes/:id/take" element={<QuizTaking />} />
                    <Route path="/quizzes/:id/result" element={<QuizResult />} />
                    <Route path="/forum" element={<Forum />} />
                    <Route path="/forum/threads/:threadId" element={<ForumThread />} />
                    <Route path="/chat" element={<Chat />} />
                    <Route path="/payments" element={<PaymentHistory />} />
                </Routes>
            </main>
            {!isFullscreen && <Footer />}
        </div>
    );
};

const App = () => {
    const [user, dispatch] = useReducer(MyUserReducer, cookies.load('user') || null);

    return (
        <MyUserContext.Provider value={[user, dispatch]}>
            <BrowserRouter>
                <AppLayout />
            </BrowserRouter>
        </MyUserContext.Provider>
    );
}

export default App;
