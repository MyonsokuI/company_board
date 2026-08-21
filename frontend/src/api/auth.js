import api from './axiosConfig';

export const login = async (loginId, password) => {
    const response = await api.post('/auth/login', {
        loginId,
        password,
    });
    return response.data;
};