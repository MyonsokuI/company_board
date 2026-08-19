import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Login.module.css';
import { login } from '../../api/auth';

const Login = () => {
    const [loginId, setLoginId] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        try {
            // API呼び出し
            const data = await login(loginId, password);

            // 成功時: トークン等を保存して遷移 (JWTの持ち方によって調整してください)
            // localStorage.setItem('token', data.token); 

            navigate('/board');
        } catch (error) {
            // バックエンドからのエラーメッセージを表示するか、デフォルトを表示
            setErrorMessage('ログインIDまたはパスワードが正しくありません');
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.loginBox}>
                <h2>社内連絡メモ・掲示板</h2>
                <form onSubmit={handleLogin}>
                    <div className={styles.field}>
                        <label>ログインID：</label>
                        <input
                            type="text"
                            value={loginId}
                            onChange={(e) => setLoginId(e.target.value)}
                        />
                    </div>
                    <div className={styles.field}>
                        <label>パスワード：</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button type="submit">ログイン</button>
                </form>
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}
            </div>
        </div>
    );
};

export default Login;