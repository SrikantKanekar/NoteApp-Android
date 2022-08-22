package com.example.note.util

// note filter
const val NOTE_ORDER_ASC = "ORDER_ASC"
const val NOTE_ORDER_DESC = "ORDER_DESC"
const val NOTE_FILTER_TITLE = "FILTER_TITLE"
const val NOTE_FILTER_DATE_CREATED = "FILTER_DATE"
const val ORDER_BY_ASC_DATE_UPDATED = NOTE_ORDER_ASC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_DESC_DATE_UPDATED = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_ASC_TITLE = NOTE_ORDER_ASC + NOTE_FILTER_TITLE
const val ORDER_BY_DESC_TITLE = NOTE_ORDER_DESC + NOTE_FILTER_TITLE

const val NOTE_PAGINATION_PAGE_SIZE = 1000

const val CREATE_LABELS_ACTION = "createLabels"
const val EDIT_LABELS_ACTION = "editLabels"
const val SELECT_LABELS_ACTION = "selectLabels"

// Urls
const val BASE_URL = "https://note-ktor.herokuapp.com"

// savedStateHandle
const val DETAIL_STATE = "com.example.note.DETAIL_STATE"
const val NOTES_STATE = "com.example.note.NOTES_STATE"
const val LABELS_STATE = "com.example.note.LABELS_STATE"

// Timeouts
const val NETWORK_TIMEOUT = 15000L
const val CACHE_TIMEOUT = 3000L

// Network errors
const val NETWORK_ERROR_TIMEOUT = "Network timeout"
const val NETWORK_ERROR = "Network error"
const val NETWORK_ERROR_UNKNOWN = "Unknown network error"

// Cache errors
const val CACHE_ERROR_TIMEOUT = "Cache timeout"
const val CACHE_ERROR_UNKNOWN = "Unknown cache error"

// tag
const val TAG = "APP_DEBUG"