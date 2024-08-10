import React, { useState } from 'react';
import PasswordCheckForm from '../components/user-edit/PasswordCheckForm';
import UserInfoEditForm from '../components/user-edit/UserInfoEditForm';

export default function UserEditPage() {
    const [passwordVerified, setPasswordVerified] = useState(false);
    // 비밀번호 확인 후 상태 변경
    const handlePasswordVerification = (isVerified) => {
        setPasswordVerified(isVerified);
    };

    return (
        <>
            {!passwordVerified ? (
                <PasswordCheckForm onPasswordVerified={handlePasswordVerification} />
            ) : (
                <UserInfoEditForm />
            )}
        </>
    );
}
