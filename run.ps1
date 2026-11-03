<#
.SYNOPSIS
Loads .env and starts the PEMS Spring Boot API.
#>

$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '.env'

if (-not (Test-Path -LiteralPath $envFile)) {
    Write-Error "Missing .env. Create it first with: Copy-Item example.env .env"
}

Get-Content -LiteralPath $envFile | ForEach-Object {
    $line = $_.Trim()
    if (-not $line -or $line.StartsWith('#')) { return }

    $parts = $line -split '=', 2
    if ($parts.Count -ne 2 -or -not $parts[0].Trim()) {
        Write-Warning "Ignoring invalid .env entry: $line"
        return
    }

    [Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim(), 'Process')
}

& (Join-Path $PSScriptRoot 'mvnw.cmd') spring-boot:run
