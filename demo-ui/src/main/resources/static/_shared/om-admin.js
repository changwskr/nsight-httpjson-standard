/**
 * NSIGHT OM 운영관리 포털 공통 유틸 (demo-ui)
 * om-service(8096) API를 /api/relay/OM/online 으로 호출합니다.
 */
window.OmAdmin = (function () {
  const BUSINESS_CODE = 'OM';
  const NAV_PRIMARY = [
    { id: 'dashboard', label: '운영 대시보드', href: '/om/admin/dashboard.html' },
    { id: 'transaction-log', label: '거래로그 조회', href: '/om/admin/transaction-log.html' },
    { id: 'service-catalog', label: 'ServiceId 관리', href: '/om/admin/service-catalog.html' },
    { id: 'user-auth', label: '사용자 / 권한 / 메뉴', href: '/om/admin/user-auth.html' },
    { id: 'audit-log', label: '감사로그 조회', href: '/om/admin/audit-log.html' }
  ];

  const NAV_SECONDARY = [
    { id: 'error-code', label: '오류코드 / 메시지', href: '/om/admin/error-code.html' },
    { id: 'batch', label: '배치 / 스케줄', href: '/om/admin/batch.html' },
    { id: 'health-check', label: 'Health Check', href: '/om/admin/health-check.html' },
    { id: 'system-config', label: '환경설정 조회', href: '/om/admin/system-config.html' },
    { id: 'file-download', label: '다운로드 이력', href: '/om/admin/file-download.html' }
  ];

  const NAV_TERTIARY = [
    { id: 'common-code', label: '공통코드 관리', href: '/om/admin/common-code.html' },
    { id: 'function-auth', label: '기능권한', href: '/om/admin/function-auth.html' },
    { id: 'data-auth', label: '데이터권한', href: '/om/admin/data-auth.html' },
    { id: 'auth-history', label: '권한이력', href: '/om/admin/auth-history.html' },
    { id: 'cache', label: 'Cache 관리', href: '/om/admin/cache.html' }
  ];

  const NAV = [...NAV_PRIMARY, ...NAV_SECONDARY, ...NAV_TERTIARY];

  const TX = {
    dashboard: { serviceId: 'OM.Dashboard.inquiry', transactionCode: 'OM-DSH-0001' },
    transactionLog: { serviceId: 'OM.TransactionLog.inquiry', transactionCode: 'OM-TXL-0001' },
    serviceCatalog: { serviceId: 'OM.ServiceCatalog.inquiry', transactionCode: 'OM-SVC-0001' },
    user: { serviceId: 'OM.User.inquiry', transactionCode: 'OM-USR-0001' },
    menu: { serviceId: 'OM.Menu.inquiry', transactionCode: 'OM-MNU-0001' },
    authGroup: { serviceId: 'OM.AuthGroup.inquiry', transactionCode: 'OM-AUT-0001' },
    auditLog: { serviceId: 'OM.AuditLog.inquiry', transactionCode: 'OM-AUD-0001' },
    errorCode: { serviceId: 'OM.ErrorCode.inquiry', transactionCode: 'OM-ERR-0001' },
    batch: { serviceId: 'OM.Batch.inquiry', transactionCode: 'OM-BAT-0001' },
    healthCheck: { serviceId: 'OM.HealthCheck.inquiry', transactionCode: 'OM-HLT-0001' },
    systemConfig: { serviceId: 'OM.SystemConfig.inquiry', transactionCode: 'OM-CFG-0001' },
    fileDownload: { serviceId: 'OM.FileDownload.inquiry', transactionCode: 'OM-FIL-0001' },
    commonCode: { serviceId: 'OM.CommonCode.inquiry', transactionCode: 'OM-CDC-0001' },
    commonCodeSave: { serviceId: 'OM.CommonCode.save', transactionCode: 'OM-CDC-0002' },
    errorCodeSave: { serviceId: 'OM.ErrorCode.save', transactionCode: 'OM-ERR-0002' },
    batchExecute: { serviceId: 'OM.Batch.execute', transactionCode: 'OM-BAT-0002' },
    functionAuth: { serviceId: 'OM.FunctionAuth.inquiry', transactionCode: 'OM-FAU-0001' },
    dataAuth: { serviceId: 'OM.DataAuth.inquiry', transactionCode: 'OM-DAU-0001' },
    authHistory: { serviceId: 'OM.AuthHistory.inquiry', transactionCode: 'OM-AHT-0001' },
    cache: { serviceId: 'OM.Cache.inquiry', transactionCode: 'OM-CCH-0001' },
    cacheDelete: { serviceId: 'OM.Cache.delete', transactionCode: 'OM-CCH-0002' }
  };

  let config = { deploymentMode: 'bootrun', bootrunHost: 'http://127.0.0.1', tomcatGatewayUrl: 'http://localhost:8080' };
  let targetUrl = '-';

  function todayIsoDate() {
    const now = new Date();
    const y = now.getFullYear();
    const m = String(now.getMonth() + 1).padStart(2, '0');
    const d = String(now.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  function todaySystemDate() {
    return todayIsoDate().replace(/-/g, '');
  }

  function newGuid() {
    if (window.crypto && crypto.randomUUID) return crypto.randomUUID();
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
      const r = Math.random() * 16 | 0;
      return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
  }

  function nowIsoKst() {
    const now = new Date();
    const offset = now.getTime() + (9 * 60 - now.getTimezoneOffset()) * 60000;
    return new Date(offset).toISOString().replace('Z', '+09:00');
  }

  function buildRelayQuery() {
    return new URLSearchParams({
      deploymentMode: config.deploymentMode,
      bootrunHost: config.bootrunHost,
      tomcatGatewayUrl: config.tomcatGatewayUrl
    }).toString();
  }

  function buildHeader(tx, processingType) {
    const systemDate = todaySystemDate();
    return {
      systemId: 'NSIGHT-MP',
      businessCode: BUSINESS_CODE,
      serviceId: tx.serviceId,
      transactionCode: tx.transactionCode,
      processingType: processingType || 'INQUIRY',
      guid: newGuid(),
      traceId: '',
      channelId: 'WEBTOP',
      userId: 'admin01',
      branchId: '000001',
      centerId: 'DC1',
      requestTime: nowIsoKst(),
      transactionIntime: nowIsoKst(),
      systemDate,
      bizDate: systemDate,
      clientIp: '127.0.0.1'
    };
  }

  async function loadConfig() {
    const res = await fetch('/api/config');
    if (res.ok) {
      const data = await res.json();
      config.deploymentMode = data.deploymentMode || config.deploymentMode;
      config.bootrunHost = data.bootrunHost || config.bootrunHost;
      config.tomcatGatewayUrl = data.tomcatGatewayUrl || config.tomcatGatewayUrl;
    }
    const urlRes = await fetch(`/api/business-modules/${BUSINESS_CODE}/target-url?${buildRelayQuery()}`);
    if (urlRes.ok) {
      const data = await urlRes.json();
      targetUrl = data.targetUrl || targetUrl;
    }
    return config;
  }

  async function call(txKey, body, processingType) {
    const tx = typeof txKey === 'string' ? TX[txKey] : txKey;
    const request = { header: buildHeader(tx, processingType), body: body || {} };
    const res = await fetch(`/api/relay/${BUSINESS_CODE}/online?${buildRelayQuery()}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request)
    });
    const relay = await res.json();
    if (!relay.responseBody) {
      throw new Error(relay.errorMessage || `HTTP ${relay.httpStatus}: 응답 없음`);
    }
    let payload;
    try {
      payload = JSON.parse(relay.responseBody);
    } catch (e) {
      throw new Error('응답 JSON 파싱 실패');
    }
    if (relay.httpStatus >= 400) {
      let msg = payload.result?.message || payload.error;
      if (!msg && relay.httpStatus === 502) {
        msg = 'om-service(8096)에 연결할 수 없습니다. OmApplication을 먼저 실행하세요.';
      }
      throw new Error(msg || `HTTP ${relay.httpStatus}`);
    }
    if (payload.result && payload.result.status === 'ERROR') {
      throw new Error(payload.result.message || payload.result.errorMessage || '거래 오류');
    }
    return { payload, relay, body: payload.body || {} };
  }

  async function inquiry(txKey, body) {
    return call(txKey, body, 'INQUIRY');
  }

  async function mutate(txKey, body, processingType) {
    return call(txKey, body, processingType);
  }

  function field(row, name, fallback) {
    if (!row) return fallback !== undefined ? fallback : '-';
    if (row[name] != null && row[name] !== '') return row[name];
    const upper = name.toUpperCase();
    if (row[upper] != null && row[upper] !== '') return row[upper];
    const key = Object.keys(row).find(k => k.toUpperCase() === upper);
    return key ? row[key] : (fallback !== undefined ? fallback : '-');
  }

  function chipForHealth(status) {
    const s = String(status || '').toUpperCase();
    if (['NORMAL', 'UP', 'SUCCESS', 'OK'].some(v => s.includes(v))) {
      return `<span class="om-chip ok">${status || 'OK'}</span>`;
    }
    if (['WARN', 'WARNING', '주의'].some(v => s.includes(v))) {
      return `<span class="om-chip warn">${status}</span>`;
    }
    if (['FAIL', 'ERROR', 'DOWN', '장애'].some(v => s.includes(v))) {
      return `<span class="om-chip fail">${status}</span>`;
    }
    return `<span class="om-chip muted">${status || '-'}</span>`;
  }

  function chipForResult(status) {
    const s = String(status || '').toUpperCase();
    if (s.includes('SUCCESS') || s === 'OK') return `<span class="om-chip ok">${status}</span>`;
    if (s.includes('FAIL') || s.includes('ERROR') || s.includes('위반')) {
      return `<span class="om-chip fail">${status}</span>`;
    }
    return `<span class="om-chip muted">${status || '-'}</span>`;
  }

  function renderNavSection(items, pageId) {
    return items.map(item =>
      `<a href="${item.href}" class="${item.id === pageId ? 'active' : ''}">${item.label}</a>`
    ).join('');
  }

  function renderShell(pageId, title) {
    document.body.innerHTML = `
      <div class="om-admin">
        <aside class="om-sidebar">
          <div class="om-brand">
            <h1>NSIGHT OM</h1>
            <p>운영관리 포털 · Operation Management</p>
          </div>
          <nav class="om-nav">
            <div class="om-nav-section">
              <div class="om-nav-label">1차 운영관리</div>
              ${renderNavSection(NAV_PRIMARY, pageId)}
            </div>
            <div class="om-nav-section">
              <div class="om-nav-label">2차 운영관리</div>
              ${renderNavSection(NAV_SECONDARY, pageId)}
            </div>
            <div class="om-nav-section">
              <div class="om-nav-label">3차 운영관리</div>
              ${renderNavSection(NAV_TERTIARY, pageId)}
            </div>
          </nav>
          <div class="om-nav-footer">
            <a href="/om/index-multi.html">↗ API 거래 테스트</a>
            <a href="/index.html">← Demo UI 홈</a>
          </div>
        </aside>
        <main class="om-main">
          <header class="om-topbar">
            <h2>${title}</h2>
            <div class="om-topbar-meta">
              <span id="omTargetUrl" title="om-service URL">${targetUrl}</span>
            </div>
          </header>
          <div class="om-content" id="omContent">
            <div class="om-empty">불러오는 중...</div>
          </div>
        </main>
      </div>`;
    return document.getElementById('omContent');
  }

  function renderPagination(container, pageNo, pageSize, totalCount, onPage) {
    const totalPages = Math.max(1, Math.ceil(totalCount / pageSize));
    if (totalCount === 0) {
      container.innerHTML = '';
      container.hidden = true;
      return;
    }
    container.hidden = false;
    let nums = '';
    for (let i = 1; i <= totalPages; i += 1) {
      nums += `<button type="button" class="om-page-btn ${i === pageNo ? 'active' : ''}" data-page="${i}">${i}</button>`;
    }
    container.innerHTML = `
      <button type="button" class="om-page-btn" data-page="prev" ${pageNo <= 1 ? 'disabled' : ''}>PREV</button>
      ${nums}
      <button type="button" class="om-page-btn" data-page="next" ${pageNo >= totalPages ? 'disabled' : ''}>NEXT</button>
      <span style="color:var(--muted);font-size:0.85rem;margin-left:8px">${pageNo} / ${totalPages} · 총 ${totalCount}건</span>`;
    container.querySelectorAll('[data-page]').forEach(btn => {
      btn.addEventListener('click', () => {
        const v = btn.getAttribute('data-page');
        if (v === 'prev' && pageNo > 1) onPage(pageNo - 1);
        else if (v === 'next' && pageNo < totalPages) onPage(pageNo + 1);
        else if (v !== 'prev' && v !== 'next') onPage(Number(v));
      });
    });
  }

  function showError(container, message) {
    const hint = targetUrl && targetUrl !== '-'
      ? `릴레이 대상: <code>${targetUrl}</code>`
      : 'om-service(포트 8096)와 demo-ui(8099)를 함께 기동했는지 확인하세요.';
    container.innerHTML = `<div class="om-alert error">${message}<br><small>${hint}</small></div>`;
  }

  function showErrorBanner(container, message) {
    const existing = container.querySelector('.om-load-error');
    if (existing) existing.remove();
    const hint = targetUrl && targetUrl !== '-'
      ? ` (${targetUrl})`
      : '';
    const banner = document.createElement('div');
    banner.className = 'om-alert error om-load-error';
    banner.innerHTML = `${message}<br><small>om-service 재빌드 후 OmApplication을 재시작하세요.${hint}</small>`;
    container.prepend(banner);
  }

  async function pingBackend() {
    try {
      const res = await fetch(`/api/business-modules/${BUSINESS_CODE}/target-url?${buildRelayQuery()}`);
      if (!res.ok) return false;
      const data = await res.json();
      targetUrl = data.targetUrl || targetUrl;
      const el = document.getElementById('omTargetUrl');
      if (el) el.textContent = targetUrl;
      return true;
    } catch (e) {
      return false;
    }
  }

  async function initPage(pageId, title, renderFn) {
    await loadConfig();
    const container = renderShell(pageId, title);
    const ok = await pingBackend();
    if (!ok) {
      showError(container, 'demo-ui → om-service 릴레이 URL을 확인할 수 없습니다.');
      return;
    }
    try {
      await renderFn(container);
    } catch (err) {
      showError(container, err.message || String(err));
    }
  }

  return {
    NAV, TX, config, targetUrl,
    todayIsoDate, todaySystemDate, field,
    chipForHealth, chipForResult,
    inquiry, mutate, call, initPage, renderPagination, showError, showErrorBanner, loadConfig
  };
})();
