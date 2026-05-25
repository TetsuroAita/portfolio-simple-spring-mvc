window.addEventListener('DOMContentLoaded', () => {
    const hiddenInput = document.querySelector('#dateOfBirthHidden');
    const ySelect = document.querySelector('#yearSelect');
    const mSelect = document.querySelector('#monthSelect');
    const dSelect = document.querySelector('#daySelect');

    if (hiddenInput.value) {
        const dateParts = hiddenInput.value.split('-');
        // 選択肢と一致させるために今回は int に変換
        ySelect.dataset.initValue = parseInt(dateParts[0], 10);
        mSelect.dataset.initValue = parseInt(dateParts[1], 10);
        dSelect.dataset.initValue = parseInt(dateParts[2], 10);
    }

    // 月と日のピッカーを生成
    function updateMonthAndDays() {
        const year = ySelect.value || ySelect.dataset.initValue;

        // 年のピッカーを生成
        ySelect.innerHTML = '';
        const optYear = new Date().getFullYear();

        const defaultOpt = document.createElement('option');
        defaultOpt.value = '';
        defaultOpt.textContent = '-';
        ySelect.appendChild(defaultOpt);
        
        for (let y = optYear; y >= 1900; y--) {
            const opt = document.createElement('option');
            opt.value = y;
            opt.textContent = y;
            if (y == year) opt.selected = true;
            ySelect.appendChild(opt);
        }

        // 初回は initValue ２回目以降は value で初期化
        const month = mSelect.value || mSelect.dataset.initValue;
        const day = dSelect.value || dSelect.dataset.initValue;

        if (!year) {
            mSelect.value = '';
            dSelect.value = '';
            mSelect.innerHTML = `<option value=''>-</option>`;
            dSelect.innerHTML = `<option value=''>-</option>`;
            return;
        }

        if (year) {
            mSelect.innerHTML = '';
            for (let m = 0; m <= 12; m++) {
                const opt = document.createElement('option');
                opt.value = (m == 0) ? '' : m;
                opt.textContent = (m == 0) ? '-' : m;
                if (m == month) opt.selected = true;
                mSelect.appendChild(opt);
            }
        }

        if (!month) {
            dSelect.value = '';
            dSelect.innerHTML = `<option value=''>-</option>`;
            return;
        }

        const lastDay = new Date(year, month, 0).getDate();

        dSelect.innerHTML = '';
        for (let d = 0; d <= lastDay; d++) {
            const opt = document.createElement('option');
            opt.value = (d == 0) ? '' : d;
            opt.textContent = (d == 0) ? '-' : d;
            if (d == day) opt.selected = true;
            dSelect.appendChild(opt);
        }

        combineToHidden();
    }

    // dateOfBirth に変換
    function combineToHidden() {
        const y = ySelect.value ? ySelect.value : '';
        const m = mSelect.value ? String(mSelect.value).padStart(2, '0') : '';
        const d = dSelect.value ? String(dSelect.value).padStart(2, '0') : '';

        if (y && m && d) {
            hiddenInput.value = `${y}-${m}-${d}`;
        } else {
            hiddenInput.value = '';
        }

        // 初期値は空にする
        ySelect.dataset.initValue = '';
        mSelect.dataset.initValue = '';
        dSelect.dataset.initValue = '';
    }

    ySelect.addEventListener('change', updateMonthAndDays);
    mSelect.addEventListener('change', updateMonthAndDays);
    dSelect.addEventListener('change', combineToHidden);

    updateMonthAndDays();
});