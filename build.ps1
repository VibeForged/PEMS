<#
.SYNOPSIS
Builds distributable backend and frontend artifacts for PEMS.
#>

$ErrorActionPreference = 'Stop'
$projectRoot = $PSScriptRoot

Push-Location $projectRoot
try {
    & (Join-Path $projectRoot 'mvnw.cmd') clean package
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    Push-Location (Join-Path $projectRoot 'pems-frontend')
    try {
        npm run build
        if ($LASTEXITCODE -ne 0) {
            exit $LASTEXITCODE
        }
    }
    finally {
        Pop-Location
    }
}
finally {
    Pop-Location
}

Write-Host 'Build complete.'
Write-Host 'Backend: target\pems-0.0.1-SNAPSHOT.jar'
Write-Host 'Frontend: pems-frontend\dist'
