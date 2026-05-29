import axios from "axios";
import cookies from 'react-cookies'

export const endpoints = {
    'login': '/login',
    'student-register': '/register/student',
    'lecturer-register': '/register/lecturer',
    'profile': '/secure/profile',
    'resources': '/resources',
    'resource-detail': (id) => `/resources/${id}`,
    'subjects': '/subjects',
    'topics': '/topics',
    'resource-types': '/resource-types',
    'resource-tags': '/resource-tags',
    'courses': '/courses',
    'course-detail': (id) => `/courses/${id}`,
    'enrollments': (courseId) => `/secure/courses/${courseId}/enrollments`,
    'enroll-self': (courseId) => `/secure/courses/${courseId}/enroll`,
    'my-enrollments': '/secure/my-enrollments',
    'course-learn': (courseId) => `/secure/courses/${courseId}/learn`,
    'course-lessons': (courseId) => `/secure/courses/${courseId}/lessons`,
    'lesson-create': '/secure/lessons',
    'lesson-update': (id) => `/secure/lessons/${id}`,
    'lesson-delete': (id) => `/secure/lessons/${id}`,
    'quizzes': '/quizzes',
    'quiz-detail': (id) => `/quizzes/${id}`,
    'quiz-questions': (quizId) => `/secure/quizzes/${quizId}/questions`,
    'forum-categories': '/forum-categories',
    'forum-threads': '/forum-threads',
    'forum-thread-detail': (id) => `/forum-threads/${id}`,
    'forum-posts': (threadId) => `/forum-threads/${threadId}/posts`,
    'chat-rooms': '/secure/chat-rooms',
    'chat-room-detail': (id) => `/secure/chat-rooms/${id}`,
    'payments': '/secure/payments',
    'payment-detail': (id) => `/secure/payments/${id}`,
    'payment-stats': '/secure/payments/stats',
}

export const authApis = () => {
    return axios.create({
        baseURL: 'http://localhost:8080/api/',
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: 'http://localhost:8080/api/'
})