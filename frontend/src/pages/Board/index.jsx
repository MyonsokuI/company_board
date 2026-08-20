import React, { useState, useEffect } from 'react';
import { getNotices, createNotice, deleteNotice } from '../../api/notice';
import styles from './Board.module.css';

const Board = () => {
    const [notices, setNotices] = useState([]);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [importance, setImportance] = useState(2);
    const [deadline, setDeadline] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const fetchNoticeList = async () => {
        try {
            const response = await getNotices();
            // response.data が { notices: [...] } の形、または直接 [...] の形どちらでも対応できるようにする
            const noticeData = response.data.notices ? response.data.notices : response.data;
            setNotices(noticeData);
        } catch (error) {
            console.error('データの取得に失敗しました', error);
        }
    };

    useEffect(() => {
        fetchNoticeList();
    }, []);

    // 保存処理
    const handleSave = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        // クライアント側バリデーション
        if (!title || !content || !deadline) {
            setErrorMessage('未入力項目があります。');
            return;
        }

        try {
            await createNotice({
                title,
                body: content,
                priority: importance,
                publishedUntil: deadline,
                createdBy: 101, // 仮のユーザーID、実際にはログインユーザーのIDを使用する
            });
            // フォームクリア＆再描画
            handleClear();
            fetchNoticeList();
        } catch (error) {
            setErrorMessage('保存に失敗しました。');
        }
    };

    // 削除処理
    const handleDelete = async (id) => {
        if (!window.confirm('以下の内容を本当に削除しますか？')) {
            return;
        }
        try {
            await deleteNotice(id);
            fetchNoticeList();
        } catch (error) {
            alert('削除が失敗しました');
        }
    };

    // クリア処理
    const handleClear = () => {
        setTitle('');
        setContent('');
        setImportance(2);
        setDeadline('');
        setErrorMessage('');
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h1>社内連絡メモ・掲示板</h1>
                <span>ログイン者：〇〇 〇〇</span>
            </header>

            {/* 入力エリア */}
            <section className={styles.inputSection}>
                <h2>入力エリア</h2>
                <form onSubmit={handleSave}>
                    <div className={styles.field}>
                        <label>タイトル：</label>
                        <div className={styles.inputWrapper}>
                            <input
                                type="text"
                                value={title}
                                maxLength={10}
                                onChange={(e) => setTitle(e.target.value)}
                            />
                            <span className={styles.counter}>{title.length}/10</span>
                        </div>
                    </div>

                    <div className={styles.field}>
                        <label>本文：</label>
                        <div className={styles.inputWrapper}>
                            <textarea
                                value={content}
                                maxLength={200}
                                onChange={(e) => setContent(e.target.value)}
                            />
                            <span className={styles.counter}>{content.length}/200</span>
                        </div>
                    </div>

                    <div className={styles.rowField}>
                        <div>
                            <label>重要度：</label>
                            <select
                                value={importance}
                                onChange={(e) => setImportance(Number(e.target.value))}
                            >
                                <option value={1}>低</option>
                                <option value={2}>中</option>
                                <option value={3}>高</option>
                            </select>
                        </div>

                        <div>
                            <label>掲載期限：</label>
                            <input
                                type="date"
                                value={deadline}
                                onChange={(e) => setDeadline(e.target.value)}
                            />
                        </div>
                    </div>

                    {errorMessage && <p className={styles.error}>{errorMessage}</p>}

                    <div className={styles.buttonGroup}>
                        <button type="button" onClick={handleClear} className={styles.clearBtn}>
                            クリア
                        </button>
                        <button type="submit" className={styles.saveBtn}>
                            保存
                        </button>
                    </div>
                </form>
            </section>

            {/* メモ一覧 */}
            <section className={styles.listSection}>
                <h2>メモ一覧</h2>
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>タイトル</th>
                            <th>本文</th>
                            <th>重要度</th>
                            <th>投稿者</th>
                            <th>掲載期限</th>
                            <th>削除</th>
                        </tr>
                    </thead>
                    <tbody>
                        {notices.length > 0 ? (
                            notices.map((notice) => (
                                <tr key={notice.noticeId}> {/* id -> noticeId に変更 */}
                                    <td>{notice.title}</td>
                                    <td>{notice.body}</td>      {/* content -> body に変更 */}
                                    <td>{notice.priority}</td>    {/* importance -> priority に変更 */}
                                    <td>{notice.createUserName}</td> {/* author -> createUserName に変更 */}
                                    <td>{notice.publishedUntil}</td> {/* deadline -> publishedUntil に変更 */}
                                    <td>
                                        <button onClick={() => handleDelete(notice.noticeId)}>削除</button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="6" style={{ textAlign: 'center' }}>---</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </section>
        </div>
    );
};

export default Board;