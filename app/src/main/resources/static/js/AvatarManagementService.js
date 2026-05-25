// ===== 状態管理 =====
let avatarFetchController = null; //アバター管理における非同期処理内のフェッチは、この1つの変数のみで管理する(今回の各公開メソッドは同画面内で同時に複数実行されることはなく、かつ "await"、また処理に要する時間が短時間なので意図的に中止する?(他の操作をさせないように画面をロックしメッセージで促す?)ことがないので本来不要)
let currentAvatarObjectUrl = null; // アバターの表示用URLをこの変数１つで管理

// ===== 公開用トースト制御メソッド =====
export async function publicShowToast(flashMessage) {
    if (flashMessage) {
        showToast(flashMessage);
    }
    return;
}

// ===== アバター画像データ取得用の公開メソッド(人物詳細画面を取得時に１度だけ呼ばれる) =====
export async function getAvatar({ profileId, displayName, hasAvatar}) {
    
    console.log("===== アバター画像データ取得(初期描写) =====");
    console.log("アバター画像データ取得対象のprofileId: " + profileId + ", デフォルト画像表示名: " + displayName + ", アバター有無: " + (hasAvatar ? "あり" : "なし"));

    const json = sessionStorage.getItem(profileId);

    if (!hasAvatar && json) {
        console.log("アバターが削除されたため、セッションからキーを削除します");
        sessionStorage.removeItem(profileId);
    }

    if (!hasAvatar) {
        console.log("アバターが登録されていないため、取得処理は行いません");
        clearAvatar(displayName);
        return;
    }
    
    if (json) {
        const avatar = JSON.parse(json);
        console.log(avatar);

        const diffMs = Math.abs(new Date() - new Date(avatar.signedAt));

        // 20分をミリ秒に換算 (20分 * 60秒 * 1000ミリ秒)
        const twentyMinutesMs = 20 * 60 * 1000;

        if (diffMs <= twentyMinutesMs) {
            console.log("現在の表示用URLは有効期限内です");
            return setAvatar(avatar.url);
        } else {
            console.log("現在の表示用URLは期限切れです");
        }
    }
    
    return fetchAndSetAvatar({
        profileId: profileId,
        displayName: displayName
    });
}

// ===== アバター画像データ変更用の公開メソッド =====
export async function changeAvatar({ profileId, displayName, uploadFile }) {
    
    abortAvatarFetch();
    avatarFetchController = new AbortController();
    
    console.log("===== アバター画像データ変更 =====");
    console.log("アバター画像データを変更します");
    console.log("アバター画像データ変更対象のprofileId: " + profileId);
    
    const formData = new FormData();
    formData.append("file", uploadFile);
    
    const changeAvatarRequestURL = "/profile-avatar/" + encodeURIComponent(profileId); // encodeURIComponentを使う理由は引数に含まれる記号が意味を持つため
    console.log("アバター画像データ変更リクエストURL: " + changeAvatarRequestURL);
    
    
    try {
        const response = await fetch(changeAvatarRequestURL, {
            signal: avatarFetchController.signal,
            method: "POST",
            body: formData
        });

        if (response.status === 422) {
            const body = await response.json();
            const error = new Error("error");
            error.name = "422";
            error.json = body;
            throw error;
        }
        
        if (!response.ok) {
            const html = await response.text();
            const error = new Error(html);
            error.name = "server-error";
            throw error;
        }
        
        console.log("アバター画像データの変更が完了しました");
        
        return fetchAndSetAvatar({
            profileId: profileId,
            displayName: displayName,
            toastMessage: "アバターを変更しました"
        });
        
    } catch (e) {
        if (e.name === "AbortError") {
            console.log("フェッチが正常に完了しました");
            return;
        }

        if (e.name === "422") {
            const json = sessionStorage.getItem(profileId);
            const avatar = JSON.parse(json);
            setAvatar(avatar.url);
            showToast(e.json.detail, "toast--error");
            return;
        }

        if (e.name === "server-error") {
            const html = document.querySelector("html");
            html.innerHTML = e.message;
            return;
        }
    }
}

// ===== アバター画像データ削除用の公開メソッド =====
export async function deleteAvatar({ profileId, displayName }) {

    const isConfirm = confirm("アバターを初期化しますか?");

    if (isConfirm) {
        abortAvatarFetch();
        avatarFetchController = new AbortController();
        
        console.log("===== アバター画像データ削除 =====");
        console.log("アバター画像データを削除します");
        console.log("アバター画像データ削除対象のprofileId: " + profileId);
        
        
        const deleteAvatarRequestURL = "/profile-avatar/" + encodeURIComponent(profileId);
        console.log("アバター画像データ削除リクエストURL: " + deleteAvatarRequestURL);
        
        try {
            const response = await fetch(deleteAvatarRequestURL, {
                signal: avatarFetchController.signal,
                method: "DELETE"
            });
            
            if (!response.ok) {
                const html = await response.text();
                const error = new Error(html);
                error.name = "server-error";
                throw error;
            }
            
            console.log("アバター画像データの削除が完了しました");
    
            sessionStorage.removeItem(profileId);
            
            clearAvatar(displayName);
            
            showToast("アバターを削除しました");
    
        } catch (e) {
            if (e.name === "AbortError") {
                console.log("フェッチが正常に完了しました");
                return;
            }
            
            if (e.name === "server-error") {
                const html = document.querySelector("html");
                html.innerHTML = e.message;
                return;
            }
        }
    }
}

// ===== アバター用のデータをフェッチしてアバターをセット =====
async function fetchAndSetAvatar({ profileId, displayName, toastMessage = "", toastType = "toast--success" }) {

    abortAvatarFetch();
    avatarFetchController = new AbortController();
    
    console.log("===== アバター画像データ取得 =====")
    console.log("アバター画像データを取得します");

    const getAvatarRequestURL = "/profile-avatar/" + encodeURIComponent(profileId) + `?t=${new Date().getTime()}`;
    console.log("アバター画像データ取得リクエストURL: " + getAvatarRequestURL);
    

    try {
        const response = await fetch(getAvatarRequestURL, {
            signal: avatarFetchController.signal
        });

        console.log(response.status);

        if (!response.ok) {
            const html = await response.text();
            const error = new Error(html);
            error.name = "server-error";
            throw error;
        }
        
        const result = await response.json();
        const { message, data } = result;
        console.log(message);
        console.log(data);

        console.log("アバター画像データを取得しました");
        console.log("取得されたアバター画像データの表示用URL: " + data);

        const avatar = { "url": data, "signedAt": new Date() }

        sessionStorage.setItem(profileId, JSON.stringify(avatar));

        setAvatar(data);
        
        if (toastMessage) {
            showToast(toastMessage, toastType);
        }

    } catch (e) {

        if (e.name === "AbortError") {
            console.log("\"fetchAndSetAvatar\"が中断されました");
            return;
        }

        if (e.name === "server-error") {    
            const html = document.querySelector("html");
            html.innerHTML = e.message;
            return;
        }
    }
}

// ===== アバターをセットするメソッド =====
function setAvatar(avatarUrl) {

    console.log("アバターをセットします")

    const skeleton = document.querySelector("#avatar-skeleton");
    const deleteBtn = document.querySelector("#avatar-delete");
    const avatar = document.querySelector("#avatar");
    const avatarName = document.querySelector("#avatar-name");

    if (currentAvatarObjectUrl) {
        console.log("現在のアバター表示用URL \"" + currentAvatarObjectUrl + "\" を削除しました");
        URL.revokeObjectURL(currentAvatarObjectUrl); // リソースを解放？プールみたいなところに溜めない
        currentAvatarObjectUrl = null;
    }

    currentAvatarObjectUrl = avatarUrl;
    console.log("現在のアバター表示用URLを \"" + currentAvatarObjectUrl + "\" に変更しました");

    const img = new Image();
    img.onload = () => {
        avatar.style.backgroundImage = `url("${currentAvatarObjectUrl}")`;
        avatarName.textContent = "";
        avatar.classList.remove("hidden");
        deleteBtn.classList.remove("hover-area--hidden");
        skeleton.classList.add("hidden");
    };
    img.src = currentAvatarObjectUrl;

    console.log("アバターをセットしました")
}

// ===== アバターを初期化するメソッド =====
function clearAvatar(displayName) {

    const skeleton = document.querySelector("#avatar-skeleton");
    const deleteBtn = document.querySelector("#avatar-delete");
    const avatar = document.querySelector("#avatar");
    const avatarName = document.querySelector("#avatar-name");

    if (currentAvatarObjectUrl) {
        console.log("現在のアバター表示用URL \"" + currentAvatarObjectUrl + "\" を削除しました");
        URL.revokeObjectURL(currentAvatarObjectUrl);
        currentAvatarObjectUrl = null;
    }

    avatar.style.backgroundImage = "";
    avatarName.textContent = displayName;
    avatar.classList.remove("hidden");
    deleteBtn.classList.add("hover-area--hidden");
    skeleton.classList.add("hidden");
    console.log("デフォルト画像を表示しました")
}

// ===== fetchを中止 =====
function abortAvatarFetch() {
    avatarFetchController?.abort();
    avatarFetchController = null;
}

// ===== トースト表示 =====
function showToast(message, type = "toast--success") {
    const toast = document.querySelector("#toast");
    const messageElement = document.querySelector("#toast-message");
    const toastSuccess = document.querySelector("#toast-success");
    const toastFaile = document.querySelector("#toast-faile");

    toastSuccess.className = "toast-success hidden";
    toastFaile.className = "toast-faile hidden";
    messageElement.textContent = message;

    if (type === "toast--success") {
        toastSuccess.classList.remove("hidden");
    } else if (type === "toast--error") {
        toastFaile.classList.remove("hidden");
    }

    toast.classList.remove("hidden");
    setTimeout(() => toast.classList.add("hidden"), 3000);
}