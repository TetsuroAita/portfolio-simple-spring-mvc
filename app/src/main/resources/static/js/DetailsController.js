// ===== インポート =====
import { publicShowToast, getAvatar, changeAvatar, deleteAvatar } from "./AvatarManagementService.js";

// ===== 詳細画面読み込み時に一度だけアバターを読み込む =====
document.addEventListener("DOMContentLoaded", async () => {
    await getAvatar({
        profileId: profileId,
        displayName: profileFullName,
        hasAvatar: hasAvatar
    });

    await publicShowToast(flashMessage);
});

// ===== アバター変更 =====
const avatarUpdate = document.querySelector("#avatar-update");
const avatarInput = document.querySelector("#avatar-input");
if (avatarUpdate) {
    avatarUpdate.addEventListener("click", () => {
        document.querySelector("#avatar-input").click();
    });
}

if (avatarInput) {
    avatarInput.addEventListener("change", async (e) => {
        const file = e.target.files[0];
        if (!file) {
            console.log("エラー: アバターを更新できませんでした file: " + file ?? "不明");
        };
    
        await changeAvatar({
            profileId: profileId,
            displayName: profileFullName,
            uploadFile: file
        });

        // 同じデータを選択しても認識されるようにリセットする
        e.target.value = "";
    });
}

// ===== アバター削除 =====
const deleteBtn = document.querySelector("#avatar-delete");
if (deleteBtn) {
    deleteBtn.addEventListener("click", async () => {
        await deleteAvatar({
                profileId: profileId,
                displayName: profileFullName
        });
    });
}

// ===== ポップアップ =====
document.querySelector("#btn-setting").addEventListener("click", () => {
    const details = document.querySelector("#popupmenu-details");
    const backdrop = document.querySelector("#backdrop");
    
    details.className = "popupmenu-card";
    backdrop.className = "backdrop";
});

const btnAvatarInfo = document.querySelector("#btn-avatar-info");
if (btnAvatarInfo) {
    btnAvatarInfo.addEventListener("click", () => {
        const avatarInfoDetails = document.querySelector("#avatar-info-details");
        const backdrop = document.querySelector("#backdrop");
    
        avatarInfoDetails.className = "popupinfo";
        backdrop.className = "backdrop";
    });
}

document.querySelector("#backdrop").addEventListener("click", () => {
    const popupmenuDetails = document.querySelector("#popupmenu-details");
    const avatarInfoDetails = document.querySelector("#avatar-info-details");
    const backdrop = document.querySelector("#backdrop");

    popupmenuDetails.className = "popupmenu-card hidden";

    if (avatarInfoDetails) {
        avatarInfoDetails.className = "popupinfo hidden";
    }
    
    backdrop.className = "backdrop hidden";
});