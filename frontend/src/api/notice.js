import api from './axiosConfig';

export const getNotices = () => api.get('/notices');
export const createNotice = (data) => api.post('/notices', data);
export const deleteNotice = (id) => api.delete(`/notices/${id}`);