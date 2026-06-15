-- OM seed data (local)

INSERT INTO OM_AP_STATUS (AP_ID, AP_NAME, HEALTH_STATUS, CPU_USAGE_PCT, HEAP_USAGE_PCT, THREAD_COUNT, CHECKED_AT) VALUES
('ap01', 'OM-AP-01', 'NORMAL', 32.5, 58.0, 120, '2026-06-14T10:00:00+09:00'),
('ap02', 'OM-AP-02', 'NORMAL', 28.1, 52.3, 98, '2026-06-14T10:00:00+09:00');

INSERT INTO OM_DB_STATUS (DB_ID, DB_NAME, HEALTH_STATUS, POOL_USAGE_PCT, CHECKED_AT) VALUES
('RDW', 'RDW', 'NORMAL', 41.2, '2026-06-14T10:00:00+09:00'),
('ADW', 'ADW', 'WARN', 78.5, '2026-06-14T10:00:00+09:00'),
('SESSIONDB', 'SESSIONDB', 'NORMAL', 35.0, '2026-06-14T10:00:00+09:00'),
('LOGDB', 'LOGDB', 'NORMAL', 44.8, '2026-06-14T10:00:00+09:00');

INSERT INTO OM_DEPLOY_STATUS (BUSINESS_CODE, WAR_NAME, WAR_VERSION, DEPLOYED_AT, HEALTH_STATUS) VALUES
('SV', 'sv.war', '0.1.0-SNAPSHOT', '2026-06-14T09:30:00+09:00', 'UP'),
('CC', 'cc.war', '0.1.0-SNAPSHOT', '2026-06-14T09:30:00+09:00', 'UP'),
('OM', 'om.war', '0.1.0-SNAPSHOT', '2026-06-14T09:30:00+09:00', 'UP'),
('ET', 'et.war', '0.1.0-SNAPSHOT', '2026-06-14T09:30:00+09:00', 'UP');

INSERT INTO OM_AUTH_GROUP (AUTH_GROUP_ID, AUTH_GROUP_NAME, DESCRIPTION, USE_YN) VALUES
('ROLE_ADMIN', '시스템관리자', 'OM 전체 관리', 'Y'),
('ROLE_OPERATOR', '운영담당자', '거래로그/모니터링', 'Y'),
('ROLE_VIEWER', '조회자', '조회 전용', 'Y');

INSERT INTO OM_USER (USER_ID, USER_NAME, BRANCH_ID, AUTH_GROUP_ID, USE_YN, LAST_LOGIN_TIME) VALUES
('admin01', '운영관리자', '000001', 'ROLE_ADMIN', 'Y', '2026-06-14T09:15:00+09:00'),
('op01', '김운영', '001234', 'ROLE_OPERATOR', 'Y', '2026-06-14T08:50:00+09:00'),
('view01', '이조회', '001234', 'ROLE_VIEWER', 'Y', '2026-06-13T17:20:00+09:00');

INSERT INTO OM_MENU (MENU_ID, MENU_NAME, MENU_URL, PARENT_MENU_ID, SORT_ORDER, USE_YN) VALUES
('OM_DASH', '운영 대시보드', '/om/admin/dashboard.html', NULL, 1, 'Y'),
('OM_TX', '거래로그 조회', '/om/admin/transaction-log.html', NULL, 2, 'Y'),
('OM_SVC', 'ServiceId 관리', '/om/admin/service-catalog.html', NULL, 3, 'Y'),
('OM_AUTH', '사용자/권한', '/om/admin/user-auth.html', NULL, 4, 'Y'),
('OM_AUDIT', '감사로그', '/om/admin/audit-log.html', NULL, 5, 'Y'),
('OM_ERR', '오류코드 관리', '/om/admin/error-code.html', NULL, 6, 'Y'),
('OM_BAT', '배치 관리', '/om/admin/batch.html', NULL, 7, 'Y'),
('OM_HLT', 'Health Check', '/om/admin/health-check.html', NULL, 8, 'Y'),
('OM_CFG', '환경설정 조회', '/om/admin/system-config.html', NULL, 9, 'Y'),
('OM_FIL', '다운로드 이력', '/om/admin/file-download.html', NULL, 10, 'Y'),
('OM_CDC', '공통코드 관리', '/om/admin/common-code.html', NULL, 11, 'Y'),
('OM_FAU', '기능권한', '/om/admin/function-auth.html', NULL, 12, 'Y'),
('OM_DAU', '데이터권한', '/om/admin/data-auth.html', NULL, 13, 'Y'),
('OM_AHT', '권한이력', '/om/admin/auth-history.html', NULL, 14, 'Y'),
('OM_CCH', 'Cache 관리', '/om/admin/cache.html', NULL, 15, 'Y');

INSERT INTO OM_SERVICE_CATALOG (CATALOG_ID, BUSINESS_CODE, SERVICE_ID, TRANSACTION_CODE, PROCESSING_TYPE, HANDLER_CLASS, AUTH_CODE, AUDIT_YN, TIMEOUT_SEC, USE_YN, DESCRIPTION) VALUES
('CAT-001', 'SV', 'SV.Sample.inquiry', 'SV-INQ-0001', 'INQUIRY', 'SvSampleInquiryHandler', 'ROLE_SV_INQ', 'N', 5, 'Y', 'SV 샘플 조회'),
('CAT-002', 'SV', 'SV.Customer.selectSummary', 'SV-INQ-0002', 'INQUIRY', 'SvCustomerSummaryHandler', 'ROLE_SV_INQ', 'Y', 5, 'Y', '고객요약 조회'),
('CAT-003', 'OM', 'OM.Dashboard.inquiry', 'OM-DSH-0001', 'INQUIRY', 'OmDashboardInquiryHandler', 'ROLE_OM_DSH', 'N', 5, 'Y', '운영 대시보드'),
('CAT-004', 'OM', 'OM.TransactionLog.inquiry', 'OM-TXL-0001', 'INQUIRY', 'OmTransactionLogInquiryHandler', 'ROLE_OM_TXL', 'N', 10, 'Y', '거래로그 조회'),
('CAT-005', 'OM', 'OM.ServiceCatalog.inquiry', 'OM-SVC-0001', 'INQUIRY', 'OmServiceCatalogInquiryHandler', 'ROLE_OM_SVC', 'N', 5, 'Y', 'ServiceId 카탈로그'),
('CAT-006', 'OM', 'OM.User.inquiry', 'OM-USR-0001', 'INQUIRY', 'OmUserInquiryHandler', 'ROLE_OM_AUTH', 'N', 5, 'Y', '사용자 조회'),
('CAT-007', 'OM', 'OM.Menu.inquiry', 'OM-MNU-0001', 'INQUIRY', 'OmMenuInquiryHandler', 'ROLE_OM_AUTH', 'N', 5, 'Y', '메뉴 조회'),
('CAT-008', 'OM', 'OM.AuthGroup.inquiry', 'OM-AUT-0001', 'INQUIRY', 'OmAuthGroupInquiryHandler', 'ROLE_OM_AUTH', 'N', 5, 'Y', '권한그룹 조회'),
('CAT-009', 'OM', 'OM.AuditLog.inquiry', 'OM-AUD-0001', 'INQUIRY', 'OmAuditLogInquiryHandler', 'ROLE_OM_AUD', 'Y', 10, 'Y', '감사로그 조회'),
('CAT-010', 'OM', 'OM.ErrorCode.inquiry', 'OM-ERR-0001', 'INQUIRY', 'OmErrorCodeInquiryHandler', 'ROLE_OM_ERR', 'N', 5, 'Y', '오류코드 조회'),
('CAT-011', 'OM', 'OM.Batch.inquiry', 'OM-BAT-0001', 'INQUIRY', 'OmBatchInquiryHandler', 'ROLE_OM_BAT', 'N', 10, 'Y', '배치/스케줄 조회'),
('CAT-012', 'OM', 'OM.HealthCheck.inquiry', 'OM-HLT-0001', 'INQUIRY', 'OmHealthCheckInquiryHandler', 'ROLE_OM_HLT', 'N', 5, 'Y', 'Health Check 조회'),
('CAT-013', 'OM', 'OM.SystemConfig.inquiry', 'OM-CFG-0001', 'INQUIRY', 'OmSystemConfigInquiryHandler', 'ROLE_OM_CFG', 'N', 5, 'Y', '환경설정 조회'),
('CAT-014', 'OM', 'OM.FileDownload.inquiry', 'OM-FIL-0001', 'INQUIRY', 'OmFileDownloadInquiryHandler', 'ROLE_OM_FIL', 'Y', 10, 'Y', '파일 다운로드 이력'),
('CAT-015', 'OM', 'OM.CommonCode.inquiry', 'OM-CDC-0001', 'INQUIRY', 'OmCommonCodeInquiryHandler', 'ROLE_OM_CDC', 'N', 5, 'Y', '공통코드 조회'),
('CAT-016', 'OM', 'OM.CommonCode.save', 'OM-CDC-0002', 'UPDATE', 'OmCommonCodeSaveHandler', 'ROLE_OM_CDC', 'Y', 5, 'Y', '공통코드 저장'),
('CAT-017', 'OM', 'OM.ErrorCode.save', 'OM-ERR-0002', 'UPDATE', 'OmErrorCodeSaveHandler', 'ROLE_OM_ERR', 'Y', 5, 'Y', '오류코드 저장'),
('CAT-018', 'OM', 'OM.Batch.execute', 'OM-BAT-0002', 'EXECUTE', 'OmBatchExecuteHandler', 'ROLE_OM_BAT', 'Y', 30, 'Y', '배치 재실행'),
('CAT-019', 'OM', 'OM.FunctionAuth.inquiry', 'OM-FAU-0001', 'INQUIRY', 'OmFunctionAuthInquiryHandler', 'ROLE_OM_AUTH', 'N', 5, 'Y', '기능권한 조회'),
('CAT-020', 'OM', 'OM.DataAuth.inquiry', 'OM-DAU-0001', 'INQUIRY', 'OmDataAuthInquiryHandler', 'ROLE_OM_AUTH', 'N', 5, 'Y', '데이터권한 조회'),
('CAT-021', 'OM', 'OM.AuthHistory.inquiry', 'OM-AHT-0001', 'INQUIRY', 'OmAuthHistoryInquiryHandler', 'ROLE_OM_AUTH', 'Y', 10, 'Y', '권한이력 조회'),
('CAT-022', 'OM', 'OM.Cache.inquiry', 'OM-CCH-0001', 'INQUIRY', 'OmCacheInquiryHandler', 'ROLE_OM_CACHE', 'N', 5, 'Y', 'Cache 조회'),
('CAT-023', 'OM', 'OM.Cache.delete', 'OM-CCH-0002', 'DELETE', 'OmCacheDeleteHandler', 'ROLE_OM_CACHE', 'Y', 10, 'Y', 'Cache 삭제');

INSERT INTO OM_TX_LOG (LOG_ID, TX_TIME, BUSINESS_CODE, SERVICE_ID, TRANSACTION_CODE, GUID, TRACE_ID, USER_ID, BRANCH_ID, RESULT_STATUS, RESULT_CODE, ERROR_CODE, ELAPSED_TIME_MS) VALUES
('TX-001', '2026-06-14T10:31:22+09:00', 'SV', 'SV.Customer.selectSummary', 'SV-INQ-0002', '7f9c1111-e29b-41d4-a716-446655440001', 'trc-7f9c1111', 'U001', '001234', 'FAIL', 'E0001', 'E-SV-DB-0001', 3210),
('TX-002', '2026-06-14T10:30:55+09:00', 'SV', 'SV.Sample.inquiry', 'SV-INQ-0001', '7f9c2222-e29b-41d4-a716-446655440002', 'trc-7f9c2222', 'U123456', '001234', 'SUCCESS', 'S0000', NULL, 42),
('TX-003', '2026-06-14T10:29:10+09:00', 'CM', 'CM.Campaign.list', 'CM-INQ-0001', '7f9c3333-e29b-41d4-a716-446655440003', 'trc-7f9c3333', 'U123456', '001234', 'SUCCESS', 'S0000', NULL, 128),
('TX-004', '2026-06-14T10:28:01+09:00', 'OM', 'OM.TransactionLog.inquiry', 'OM-TXL-0001', '7f9c4444-e29b-41d4-a716-446655440004', 'trc-7f9c4444', 'admin01', '000001', 'SUCCESS', 'S0000', NULL, 95),
('TX-005', '2026-06-14T10:25:44+09:00', 'MG', 'MG.Message.send', 'MG-EXE-0001', '7f9c5555-e29b-41d4-a716-446655440005', 'trc-7f9c5555', 'U123456', '001234', 'FAIL', 'E0001', 'E-MG-TMO-0001', 5001);

INSERT INTO OM_AUDIT_LOG (AUDIT_ID, AUDIT_TIME, USER_ID, BRANCH_ID, CUSTOMER_NO, FUNCTION_ID, FUNCTION_NAME, INQUIRY_REASON, RESULT_STATUS, CLIENT_IP) VALUES
('AUD-001', '2026-06-14T10:20:00+09:00', 'U123456', '001234', 'CUST-0001', 'CUSTOMER_INQUIRY', '고객정보 조회', '고객상담', 'SUCCESS', '10.10.10.10'),
('AUD-002', '2026-06-14T10:15:00+09:00', 'U123456', '001234', 'CUST-0002', 'DOWNLOAD', '리포트 다운로드', '캠페인 분석', 'SUCCESS', '10.10.10.10'),
('AUD-003', '2026-06-14T09:55:00+09:00', 'view01', '001234', 'CUST-0099', 'CUSTOMER_INQUIRY', '고객정보 조회', '민원', 'FAIL', '10.10.10.11');

INSERT INTO OM_ERROR_CODE (ERROR_CODE, ERROR_CATEGORY, USER_MESSAGE, OPERATOR_MESSAGE, ACTION_GUIDE, NOTIFY_TARGET, USE_YN) VALUES
('E-SV-DB-0001', 'DB', '일시적으로 조회할 수 없습니다.', '고객요약 조회 DB 오류', 'RDW 연결 및 SQL 확인', '운영팀', 'Y'),
('E-SV-BIZ-0001', 'BIZ', '조회 결과가 없습니다.', '고객요약 조회 결과 0건', '조건 확인 후 재조회', '업무팀', 'Y'),
('E-MG-TMO-0001', 'SYS', '처리 시간이 초과되었습니다.', '메시지 발송 Timeout', 'MG AP 스레드·외부 연동 확인', '운영팀/개발팀', 'Y'),
('E-OM-BIZ-0001', 'BIZ', '요청을 처리할 수 없습니다.', 'OM 업무코드 검증 실패', 'businessCode=OM 확인', '개발팀', 'Y'),
('E-OM-AUTH-0001', 'AUTH', '접근 권한이 없습니다.', '관리자 권한 부족', '권한그룹 확인', '보안팀', 'Y'),
('E-OM-VAL-0001', 'VALIDATION', '입력값을 확인해 주세요.', '필수 사유 미입력', '5자 이상 사유 입력', '개발팀', 'Y'),
('E-OM-VAL-0002', 'VALIDATION', '필수 항목이 누락되었습니다.', '필수 필드 누락', '요청 body 확인', '개발팀', 'Y'),
('E-OM-BIZ-0002', 'BIZ', '요청을 처리할 수 없습니다.', 'Cache 대상 없음', 'Cache명 확인', '운영팀', 'Y'),
('E-OM-BIZ-0003', 'BIZ', '요청을 처리할 수 없습니다.', '배치 Job 없음', 'Job ID·사용여부 확인', '운영팀', 'Y');

INSERT INTO OM_BATCH_JOB (JOB_ID, JOB_NAME, BUSINESS_CODE, CRON_EXPR, USE_YN, DESCRIPTION) VALUES
('BAT-SV-001', 'SV 일별 집계', 'SV', '0 2 * * *', 'Y', '통합고객 일별 집계'),
('BAT-CM-001', 'CM 캠페인 반응 집계', 'CM', '0 3 * * *', 'Y', '캠페인 반응률 집계'),
('BAT-LOG-001', '거래로그 아카이빙', 'ET', '0 4 * * 0', 'Y', 'LOGDB → 아카이브 이관'),
('BAT-OM-001', 'OM Health 스냅샷', 'OM', '0 */10 * * *', 'Y', 'AP/DB 상태 스냅샷');

INSERT INTO OM_BATCH_HISTORY (HISTORY_ID, JOB_ID, RUN_TIME, RUN_STATUS, DURATION_MS, RESULT_MESSAGE) VALUES
('BH-001', 'BAT-SV-001', '2026-06-14T02:00:15+09:00', 'SUCCESS', 45200, '집계 완료 12,340건'),
('BH-002', 'BAT-CM-001', '2026-06-14T03:00:08+09:00', 'SUCCESS', 12800, '캠페인 24건 처리'),
('BH-003', 'BAT-LOG-001', '2026-06-08T04:00:00+09:00', 'FAIL', 900001, '아카이브 대상 테이블 Lock'),
('BH-004', 'BAT-OM-001', '2026-06-14T10:00:00+09:00', 'SUCCESS', 850, '스냅샷 6건 갱신');

INSERT INTO OM_SYSTEM_CONFIG (CONFIG_KEY, CONFIG_CATEGORY, CONFIG_VALUE, EDITABLE_YN, DESCRIPTION) VALUES
('server.port', 'application', '8096', 'N', 'OM bootRun 포트 (화면 직접 수정 금지)'),
('spring.datasource.hikari.maximum-pool-size', 'hikari', '10', 'N', 'Hikari Pool Size (조회만)'),
('mybatis.configuration.default-statement-timeout', 'mybatis', '3', 'N', 'MyBatis 기본 Timeout(초)'),
('nsight.timeout.online-transaction-seconds', 'application', '5', 'N', '온라인 거래 Timeout(초)'),
('server.tomcat.threads.max', 'tomcat', '200', 'N', 'Tomcat 최대 스레드 (권장값 비교용)'),
('server.tomcat.threads.max.recommended', 'tomcat', '200', 'N', 'Tomcat 권장 최대 스레드');

INSERT INTO OM_FILE_DOWNLOAD_LOG (LOG_ID, DOWNLOAD_TIME, USER_ID, FILE_NAME, FILE_SIZE, BUSINESS_CODE, RESULT_STATUS, CLIENT_IP) VALUES
('FDL-001', '2026-06-14T11:00:00+09:00', 'U123456', 'campaign_report_20260614.xlsx', 2048576, 'CM', 'SUCCESS', '10.10.10.10'),
('FDL-002', '2026-06-14T10:45:00+09:00', 'op01', 'tx_log_export.csv', 512000, 'OM', 'SUCCESS', '10.10.10.12'),
('FDL-003', '2026-06-13T16:30:00+09:00', 'view01', 'customer_list.pdf', 0, 'SV', 'FAIL', '10.10.10.11');

INSERT INTO OM_COMMON_CODE (CODE_GROUP, CODE, CODE_NAME, SORT_ORDER, USE_YN, DESCRIPTION) VALUES
('BUSINESS_CODE', 'SV', 'Single View', 1, 'Y', '통합고객 조회'),
('BUSINESS_CODE', 'CM', 'Campaign', 2, 'Y', '캠페인'),
('BUSINESS_CODE', 'OM', 'Operation Mgmt', 3, 'Y', '운영관리'),
('CHANNEL_CODE', 'WEBTOP', '웹탑', 1, 'Y', '웹 채널'),
('CHANNEL_CODE', 'MOBILE', '모바일', 2, 'Y', '모바일 채널'),
('CENTER_CODE', 'DC1', '데이터센터1', 1, 'Y', '주 DC');

INSERT INTO OM_FUNCTION_AUTH (AUTH_ID, AUTH_GROUP_ID, MENU_ID, CAN_INQUIRY, CAN_REGISTER, CAN_UPDATE, CAN_DELETE, CAN_DOWNLOAD) VALUES
('FA-001', 'ROLE_ADMIN', 'OM_DASH', 'Y', 'Y', 'Y', 'Y', 'Y'),
('FA-002', 'ROLE_ADMIN', 'OM_TX', 'Y', 'N', 'N', 'N', 'Y'),
('FA-003', 'ROLE_OPERATOR', 'OM_TX', 'Y', 'N', 'N', 'N', 'Y'),
('FA-004', 'ROLE_VIEWER', 'OM_DASH', 'Y', 'N', 'N', 'N', 'N');

INSERT INTO OM_DATA_AUTH (AUTH_ID, AUTH_GROUP_ID, BRANCH_SCOPE, CUSTOMER_ACCESS_LEVEL, DESCRIPTION) VALUES
('DA-001', 'ROLE_ADMIN', 'ALL', 'FULL', '전 지점·전체 고객정보'),
('DA-002', 'ROLE_OPERATOR', 'OWN_BRANCH', 'MASKED', '소속 지점·마스킹 조회'),
('DA-003', 'ROLE_VIEWER', 'OWN_BRANCH', 'SUMMARY_ONLY', '소속 지점·요약만');

INSERT INTO OM_AUTH_HISTORY (HISTORY_ID, CHANGED_AT, CHANGED_BY, TARGET_TYPE, TARGET_ID, BEFORE_VALUE, AFTER_VALUE, CHANGE_REASON) VALUES
('AH-001', '2026-06-13T14:00:00+09:00', 'admin01', 'USER_AUTH', 'op01', 'ROLE_VIEWER', 'ROLE_OPERATOR', '운영담당 업무 배정'),
('AH-002', '2026-06-12T11:30:00+09:00', 'admin01', 'ERROR_CODE', 'E-SV-BIZ-0001', '조회 결과 없음', '조회 결과가 없습니다.', '사용자 메시지 표준화');

INSERT INTO OM_CACHE_STATUS (CACHE_NAME, CACHE_KEY, ENTRY_COUNT, LAST_UPDATED, TTL_SEC) VALUES
('serviceCatalog', '*', 14, '2026-06-14T10:00:00+09:00', 3600),
('commonCode', 'BUSINESS_CODE', 3, '2026-06-14T09:00:00+09:00', 1800),
('sessionRegion', 'DC1', 120, '2026-06-14T10:30:00+09:00', 600);
