import { publicShowToast } from "./AvatarManagementService.js";

document.addEventListener("DOMContentLoaded", async () => {
    await publicShowToast(flashMessage);
});

document.querySelector("#btn-setting").addEventListener("click", () => {
    const list = document.querySelector("#popupmenu-list");
    const backdrop = document.querySelector("#backdrop");
    
    list.className = "popupmenu-card";
    backdrop.className = "backdrop";
});

document.querySelector("#backdrop").addEventListener("click", () => {
    const list = document.querySelector("#popupmenu-list");
    const backdrop = document.querySelector("#backdrop");

    list.className = "popupmenu-card hidden";
    backdrop.className = "backdrop hidden";
});