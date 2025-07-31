// ダークモード管理スクリプト
(function() {
    'use strict';

    // テーマの初期化
    function initTheme() {
        const savedTheme = localStorage.getItem('theme');
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        const theme = savedTheme || (prefersDark ? 'dark' : 'light');
        
        document.documentElement.setAttribute('data-bs-theme', theme);
        updateToggleButton(theme);
    }

    // トグルボタンの表示更新
    function updateToggleButton(theme) {
        const toggleButton = document.getElementById('dark-mode-toggle');
        if (toggleButton) {
            toggleButton.textContent = theme === 'dark' ? '🌞' : '🌙';
            toggleButton.setAttribute('aria-label', theme === 'dark' ? 'ライトモードに切替' : 'ダークモードに切替');
        }
    }

    // テーマ切替
    function toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-bs-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        
        document.documentElement.setAttribute('data-bs-theme', newTheme);
        localStorage.setItem('theme', newTheme);
        updateToggleButton(newTheme);
    }

    // DOMロード後に初期化
    document.addEventListener('DOMContentLoaded', function() {
        initTheme();
        
        // トグルボタンのイベントリスナー設定
        const toggleButton = document.getElementById('dark-mode-toggle');
        if (toggleButton) {
            toggleButton.addEventListener('click', toggleTheme);
        }
    });

    // システムテーマ変更の監視
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function(e) {
        if (!localStorage.getItem('theme')) {
            const theme = e.matches ? 'dark' : 'light';
            document.documentElement.setAttribute('data-bs-theme', theme);
            updateToggleButton(theme);
        }
    });
})();